package app.inex.inexfilms.presentation.filmdetails.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import app.inex.inexfilms.App
import app.inex.inexfilms.R
import app.inex.inexfilms.core.common.ext.isNotNull
import app.inex.inexfilms.core.common.ext.withBaseUrl
import app.inex.inexfilms.core.common.utils.uiLazy
import app.inex.inexfilms.core.ui.base.BaseMvvmFragment
import app.inex.inexfilms.core.ui.dialog.ErrorDialog
import app.inex.inexfilms.core.ui.ext.*
import app.inex.inexfilms.data.models.PlayerPlaylist
import app.inex.inexfilms.data.models.PlayerSource
import app.inex.inexfilms.databinding.FragmentFilmDetailsBinding
import app.inex.inexfilms.presentation.filmdetails.adapter.EpisodesAdapter
import app.inex.inexfilms.presentation.filmdetails.adapter.SeasonsAdapter
import app.inex.inexfilms.presentation.filmdetails.vm.FilmDetailsViewAction
import app.inex.inexfilms.presentation.filmdetails.vm.FilmDetailsViewModel
import app.inex.inexfilms.presentation.filmdetails.vm.FilmDetailsViewState
import coil.load
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import javax.inject.Inject
import kotlin.math.max

class FilmDetailsFragment: BaseMvvmFragment<FilmDetailsViewState, FilmDetailsViewAction, FilmDetailsViewModel>(
    layoutResId = R.layout.fragment_film_details
) {

    private val binding by viewBinding(FragmentFilmDetailsBinding::bind)

    @Inject
    lateinit var viewModelFactory: FilmDetailsViewModel.Factory

    override val viewModel: FilmDetailsViewModel by viewModels {
        viewModelFactory.create(filmPage = filmPage)
    }

    private val filmPage: String by uiLazy {
        requireArguments().getString(ARG_FILM_PAGE).orEmpty()
    }

    private var playerCurrentPosition: Long = 0
    private var playerPlayWhenReady: Boolean = false

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(requireContext()).build()
    }

    private lateinit var selectEpisodeBarLl: LinearLayout
    private lateinit var seasonsSpinner: Spinner
    private lateinit var episodesSpinner: Spinner

    private val seasonsAdapter
        get() = seasonsSpinner.adapter as SeasonsAdapter

    private val episodesAdapter
        get() = episodesSpinner.adapter as EpisodesAdapter

    override fun setupInjection() = App.component.inject(fragment = this)

    override fun setupUi() {
        binding.playerView.player = exoPlayer
        setupPlayer()

        binding.errorFrame.repeatBtn.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun setupPlayer() {
        with(binding) {
            selectEpisodeBarLl = playerView.findViewById(R.id.select_episode_bar_ll)
            seasonsSpinner = playerView.findViewById(R.id.seasons_spinner)
            episodesSpinner = playerView.findViewById(R.id.episodes_spinner)

            exoPlayer.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    setPlayerFullscreen(exoPlayer.playWhenReady)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    playerView.keepScreenOn = (
                        playbackState == Player.STATE_IDLE  ||
                        playbackState == Player.STATE_ENDED ||
                        playbackState != Player.STATE_READY
                    ).not()
                }
            })
        }
    }

    private fun setPlayerFullscreen(fullscreen: Boolean) {
        binding.playerView.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = if (fullscreen)
                ConstraintLayout.LayoutParams.MATCH_PARENT
            else
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        }
    }

    private fun releasePlayer() {
        playerCurrentPosition = exoPlayer.currentPosition
        playerPlayWhenReady = exoPlayer.playWhenReady
        exoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(KEY_PLAYER_CUR_POS, max(0, exoPlayer.currentPosition))
        outState.putBoolean(KEY_PLAYER_IS_READY, exoPlayer.playWhenReady)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            playerCurrentPosition = savedInstanceState.getLong(KEY_PLAYER_CUR_POS, playerCurrentPosition)
            playerPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAYER_IS_READY, playerPlayWhenReady)
        }
    }

    override fun proceedAction(action: FilmDetailsViewAction) {
        when(action) {
            is FilmDetailsViewAction.DialogError -> ErrorDialog.newInstance(errorModel = action.error)
                .show(childFragmentManager, null)
        }
    }

    override fun setupRender() {
        with(viewModel.uiState) {
            renderIn(lifecycle, { it.isLoading }, ::renderLoading)
            renderIn(lifecycle, { it.content }, ::renderContent)
            renderIn(lifecycle, { it.film }, ::renderFilm)
            renderIn(lifecycle, { it.serial }, ::renderSerial)
            renderIn(lifecycle, { it.error }, ::renderError)
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        with(binding) {
            loadingFrame.root.isVisible = isLoading
        }
    }

    private fun renderContent(content: FilmDetailsViewState.Content?) {
        binding.contentGroup.isVisible = content.isNotNull()

        content ?: return

        with(binding) {
            posterIv.load(content.poster.withBaseUrl())
            nameTv.text = content.name
        }
    }

    private fun renderFilm(film: PlayerSource?) {
        film ?: return

        preparePlayer(film.uri)
    }

    private fun renderSerial(serial: PlayerPlaylist?) {
        serial ?: return

        selectEpisodeBarLl.makeVisible()

        SeasonsAdapter(context = requireContext()).let { adapter ->
            adapter.setSeasons(seasons = serial.seasons)
            seasonsSpinner.onItemSelectedListener = onSeasonSelectedListener
            seasonsSpinner.adapter = adapter
        }

        EpisodesAdapter(context = requireContext()).let { adapter ->
            episodesSpinner.onItemSelectedListener = onEpisodeSelectedListener
            episodesSpinner.adapter = adapter
        }
    }

    private fun renderError(error: String?) {
        with(binding) {
            errorFrame.root.isVisible = error.isNotNull()
            errorFrame.errorTv.text = error.orEmpty()
        }
    }

    private val onSeasonSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val episodes = seasonsAdapter.getSeason(position).episodes
            episodesAdapter.setEpisodes(episodes)

            if (episodes.isNotEmpty()) {
                episodesSpinner.setSelection(0)
                preparePlayer(episodes.first().uri)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private val onEpisodeSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val episode = episodesAdapter.getEpisode(position)
            preparePlayer(episode.uri)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    fun preparePlayer(uri: String?) {
        if (uri == null) {
            ErrorDialog.newInstance(
                message = requireContext().getString(R.string.media_missing_error)
            ).show(childFragmentManager, null)
            return
        }

        exoPlayer.setMediaItem(MediaItem.fromUri(uri.orEmpty()))
        exoPlayer.playWhenReady = playerPlayWhenReady
        exoPlayer.seekTo(playerCurrentPosition)
        exoPlayer.prepare()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) {
        when(keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                binding.playerView.showController()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    companion object {
        fun newInstance(
            filmPage: String
        ): FilmDetailsFragment = FilmDetailsFragment().withArgs {
            putString(ARG_FILM_PAGE, filmPage)
        }

        private const val ARG_FILM_PAGE = "ARG_FILM_PAGE"

        private const val KEY_PLAYER_CUR_POS = "KEY_PLAYER_CUR_POS"
        private const val KEY_PLAYER_IS_READY = "KEY_PLAYER_IS_READY"
    }
}