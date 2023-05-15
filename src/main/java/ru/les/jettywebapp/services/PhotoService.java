package ru.les.jettywebapp.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.les.jettywebapp.dao.PhotoDAO;
import ru.les.jettywebapp.models.Prediction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

@Service
@NoArgsConstructor
public class PhotoService {
    PhotoDAO photoDAO;
    private Integer ID = 0;

    @Autowired
    public PhotoService(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    public Integer savePhoto(MultipartFile photo) {
        try {
            photoDAO.savePhoto(++ID, Base64.getEncoder().encodeToString(photo.getBytes()));
            return ID;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ID;
    }


    public synchronized Prediction handle(MultipartFile file) {
        return runPythonScript(savePhoto(file));
    }

    private Prediction runPythonScript(Integer id) {

        try {
//            ProcessBuilder condaProcessBuilder = new ProcessBuilder("conda", "activate", "easyocr");
//            condaProcessBuilder.redirectErrorStream(true);
//
//            Process condaProcess = condaProcessBuilder.start();
//            condaProcess.waitFor();
            //ProcessBuilder processBuilder = new ProcessBuilder("python3", "/python/script.py", id.toString());
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\David\\AppData\\Local\\Programs\\Python\\Python310\\python.exe", "C:\\lespytest\\test.py", id.toString());
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            bufferedReader.close();
            Prediction prediction = photoDAO.getAndDeletePrediction(id);
            System.out.println(prediction);
            return prediction;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /*public Prediction getPredictionById(Integer id) {
        return photoDAO.getAndDeletePrediction(id);
    }*/
}
