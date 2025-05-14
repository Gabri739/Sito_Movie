package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;

/*La classe MovieController gestisce le richieste HTTP e fa in modo che venngano messi i dati a disposizione per la visualizzazione per produrre una risposta */
@Controller
public class MovieController {

	@Autowired //Autowired permette di creare un oggetto MovieRepository senza passare per definizione ed invocazione del costruttore della classe in questione
	MovieService movieService;
	@Autowired
	ArtistService artistService;

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));
		return "movie.html";
	}
	@GetMapping("/movie")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "movies.html";
	}
	@GetMapping("/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "formNewMovie.html";
	}
	@PostMapping("/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()){
			model.addAttribute("messaggioErroreTitolo", "Campo obbligatorio");
			return "formNewMovie.html";
		}else {
			this.movieService.save(movie);
			model.addAttribute("movie", movie);
			return "redirect:/movie/"+movie.getId();
		}
	}
	@GetMapping("/aggiornaFilm")
	public String aggionaFilm(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "aggiornaFilm.html";
	}
	@GetMapping("/cancellaFilm/{id}")
	public String cancellaFilm(@PathVariable("id") Long id, Model model) {
		this.movieService.deleteById(id);
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "aggiornaFilm.html";
	}
	@GetMapping("/modificaFilm/{id}")
	public String modificaFilm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));
		return "modificaFilm.html";
	}
	@GetMapping("/registaPerFilm/{id}")
	public String aggiungiRegistaPerFilm(@PathVariable("id") Long id, Model model){
		model.addAttribute("artists", this.artistService.getAllArtists());
		model.addAttribute("movie",this.movieService.getMovieById(id));
		return "registaPerFilm.html";
	}

	@GetMapping("/aggiungiRegistaFilm/{movieId}/{artistId}")
	public String aggiungiRegista(@PathVariable("movieId") Long movieId, @PathVariable("artistId") Long artistId, Model model) {
		this.movieService.addDirectorToMovie(movieId, artistId);
		model.addAttribute("movie",this.movieService.getMovieById(movieId));
		return"redirect:/modificaFilm/" + movieId;
	}

	@GetMapping("/aggiornaAttori/{id}")
	public String aggiornaAttoriPerFilm(@PathVariable("id") Long id, Model model) {
		Movie movie=this.movieService.getMovieById(id);
		model.addAttribute("movie",movie);
		model.addAttribute("attoriFilm", movie.getActors());
		List<Artist> liberi = (List<Artist>) this.artistService.getAllArtists(); 
		liberi.removeAll(movie.getActors()); 
		model.addAttribute("attoriLiberi", liberi);
		return "aggiornaAttori.html";
	}

	@GetMapping("/aggiungiAttoreAFilm/{movieId}/{artistId}")
	public String aggiungiAttore(@PathVariable("movieId") Long movieId, @PathVariable("artistId") Long artistId, Model model) {
		this.movieService.addActorToMovie(movieId, artistId);
		Movie movie=this.movieService.getMovieById(movieId);
		model.addAttribute("movie",movie);
		model.addAttribute("attoriFilm", movie.getActors());
		List<Artist> liberi = (List<Artist>) this.artistService.getAllArtists();
		liberi.removeAll(movie.getActors()); 
		model.addAttribute("attoriLiberi", liberi);
		return "aggiornaAttori.html";
	}
}