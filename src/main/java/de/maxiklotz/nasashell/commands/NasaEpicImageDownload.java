package de.maxiklotz.nasashell.commands;


import de.maxiklotz.nasashell.utils.Utils;
import de.maxiklotz.nasashell.dto.ImageMetadata;
import de.maxiklotz.nasashell.dto.NasaDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@ShellComponent
@Slf4j
public class NasaEpicImageDownload {

    @Value("${nasa.api.key}")
    private String apiKey;

    @Value("${nasa.api.downloadimageurl}")
    private String pictureDownloadUrl;

    @Value("${nasa.api.imagedataurl}")
    private String imageDataUrl;

    @Value("${nasa.api.placeholder.date}")
    private String datePlaceholder;

    @Value("${nasa.api.placeholder.imagename}")
    private String imageNamePlaceholder;

    @Value("${nasa.api.placeholder.api.key}")
    private String apiKeyPlaceHolder;

    @Value("${nasa.api.availabledatesurl}")
    private String availableDatesUrl;

    private Utils utils;

    @Autowired
    public void setUtils(Utils utils){
        this.utils = utils;
    }

    @ShellMethod(key = "download-images")
    public void downloadImages(@ShellOption(value = "--date", defaultValue = "") String date,
                               @ShellOption(value = "--targetFolder") String targetFolder) {
        if(!StringUtils.hasText(apiKey)){
            throw new IllegalArgumentException("API-Key not specified!");
        }
        try{
            if(date.isEmpty()){
                date = getLatestAvailableDate();
            } else {
                utils.checkDateFormatOrThrow(date);
            }
            String folderToSavePicturesIn = createTargetFolder(targetFolder, date);
            List<ImageMetadata> imageDataByDate = getImageDataByDate(date);
            for(ImageMetadata imageMetadata : imageDataByDate){
                downloadPicture(folderToSavePicturesIn, imageMetadata.getImage(), date);
            }
            log.info("Downloaded images to folder: " + folderToSavePicturesIn);
        } catch (IOException ex){
            log.error("Could not save picture");
        } catch (IllegalArgumentException ex){
            log.error("Given date is not valid", ex);
        }

    }

    private void downloadPicture(String folderToSavePicturesIn, String image, String date) throws IOException {
        String uri= pictureDownloadUrl.replace(datePlaceholder, date.replace("-","/")).replace(imageNamePlaceholder, image)
                .replace(apiKeyPlaceHolder, apiKey);
        byte[] file = RestClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .body(byte[].class);
        String imagename = image + ".png";
        File outputFile = new File(folderToSavePicturesIn,imagename);
        if(file == null){

            throw new NoSuchElementException("Did not receive picture data from api!");
        }
        Files.write(outputFile.toPath(),file);
    }


    private String createTargetFolder(String targetFolder, String dateFolder) throws IOException {
        try{
            Path pathToTargetFolder = Path.of(targetFolder, dateFolder);
            Files.createDirectories(pathToTargetFolder);
            log.info("Created target folder: " + pathToTargetFolder.toAbsolutePath());
            return pathToTargetFolder.toString();
        } catch (IOException ex){
            throw new IOException("Unable to create target folder", ex);
        }
    }

    private List<ImageMetadata> getImageDataByDate(String date) throws RestClientResponseException {
        String uri = imageDataUrl.replace(datePlaceholder, date).replace(apiKeyPlaceHolder, apiKey);
        return RestClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private String getLatestAvailableDate() throws RestClientResponseException{
        List<NasaDate> dates = RestClient.create()
                .get()
                .uri(availableDatesUrl.replace(apiKeyPlaceHolder, apiKey))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        if(dates == null || dates.isEmpty()){
            throw new NoSuchElementException("No date has been found!");
        }
        return dates.stream().map(NasaDate::getDate).sorted(Comparator.reverseOrder()).toList().get(0).toString();
    }

}
