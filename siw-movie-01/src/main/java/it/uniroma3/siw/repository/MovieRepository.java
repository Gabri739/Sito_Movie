package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Movie;
import jakarta.transaction.Transactional;

/*L'interface MocvieRepository mi consente di gestire ed interagire con il database senza scrivere comandi SQL dirrettamente
tramite l'estensione di CrudRepository mi permette di getire le operazioni base come: Creazione di un nuovo elemento, Recupero delle info,
aggiornamento dei dati e rimozione dei dati */

public interface MovieRepository extends CrudRepository<Movie, Long>{
	
    @Modifying
    @Transactional
    @Query("UPDATE Movie m SET m.director.id = :artistId WHERE m.id = :movieId AND m.director IS NULL")
    public void addDirectorToMovie(Long movieId, Long artistId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO movie_actors VALUES (:artistId, :movieId)", nativeQuery = true)
    public void addActorToMovie(Long movieId, Long artistId);

}
