package de.maxiklotz.nasashell.commands;


import de.maxiklotz.nasashell.Utils;
import de.maxiklotz.nasashell.dto.ImageMetadata;
import de.maxiklotz.nasashell.dto.NasaDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@ShellComponent
@Slf4j
public class NasaEpicImageDownload {

    @Value("${nasa.api.key}")
    private String apiKey;

    @Autowired
    private Utils utils;

    @ShellMethod(key = "download-images")
    public void downloadImages(@ShellOption(value = "--date", defaultValue = "") String date,
                               @ShellOption(value = "--targetFolder") String targetFolder) {
        if(apiKey == null || apiKey.isEmpty()){
            throw new IllegalArgumentException("API-Key not specified!");
        }
        try{
            if(date.isEmpty()){
                date = getLatestAvailableDate();
            } else {
                utils.checkDateFormat(date);
            }
            String folderToSavePicturesIn = createTargetFolder(targetFolder, date);
            List<ImageMetadata> imageDataByDate = getImageDataByDate(date);
            for(ImageMetadata imageMetadata : imageDataByDate){
                downloadPicture(folderToSavePicturesIn, imageMetadata.getImage(), date);
            }
            log.info("Downloaded images to folder: " + folderToSavePicturesIn);
        } catch (IOException ex){
            log.error("Could save picture");
        }

    }

    private void downloadPicture(String folderToSavePicturesIn, String image, String date) throws IOException {

        String uri = "https://api.nasa.gov/EPIC/archive/natural/" + date.replace("-","/") +
                "/png/" + image + ".png?api_key=" + apiKey;
        byte[] file = RestClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .body(byte[].class);
        String imagename = image + ".png";
        File outputFile = new File(folderToSavePicturesIn,imagename);
        if(file == null){
            throw new RuntimeException("Did not receive picture data from api!");
        }
        Files.write(outputFile.toPath(),file);
    }


    private String createTargetFolder(String targetFolder, String dateFolder){
        try{
            Path of = Path.of(targetFolder, dateFolder);
            Files.createDirectories(of);
            log.info("Created target folder: " + of.toAbsolutePath());
            return of.toString();
        } catch (IOException ex){
            throw new RuntimeException("Unable to create target folder", ex);
        }
    }

    private List<ImageMetadata> getImageDataByDate(String date){
        String uri = "https://api.nasa.gov/EPIC/api/natural/date/" + date + "?api_key=" + apiKey;
        return RestClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private String getLatestAvailableDate(){
        List<NasaDate> dates = RestClient.create()
                .get()
                .uri("https://api.nasa.gov/EPIC/api/natural/all?api_key=" + apiKey)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        if(dates == null || dates.isEmpty()){
            throw new RuntimeException("No date has been found!");
        }
        return dates.stream().map(NasaDate::getDate).sorted(Comparator.reverseOrder()).toList().get(0).toString();
    }

}
