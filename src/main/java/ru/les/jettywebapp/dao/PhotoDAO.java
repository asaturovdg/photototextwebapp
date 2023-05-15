package ru.les.jettywebapp.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.les.jettywebapp.models.Prediction;

@Repository
@AllArgsConstructor
public class PhotoDAO {
    private final JdbcTemplate jdbcTemplate;
    public void savePhoto(int id, String stream) {
            jdbcTemplate.update("INSERT INTO photos VALUES(?, ?)", id, stream);
    }

    public Prediction getPrediction(Integer id) {
        String text = jdbcTemplate.queryForObject("SELECT stream FROM predictions WHERE id=?", String.class, id);
        System.out.println("db sout: " + text);
        Prediction prediction = new Prediction(text, id);
        return prediction;
    }

    public Prediction getAndDeletePrediction(Integer id) {
        String text = jdbcTemplate.queryForObject("SELECT stream FROM predictions WHERE id=?", String.class, id);
        System.out.println("db sout: " + text);
        jdbcTemplate.update("DELETE FROM photos WHERE id=?", id);
        jdbcTemplate.update("DELETE FROM predictions WHERE id=?", id);
        Prediction prediction = new Prediction(text, id);
        return prediction;
    }
}
