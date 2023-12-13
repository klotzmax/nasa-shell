# Nasa Shell
A little Spring Shell program to download all images from NASA EPIC API for a given date.

To run the program you must set the API-Key in application.properties or run the tool with environment variable: nasa.api.key=YOUR_API_KEY

You can run the tool by the command 'download-images'. \
The tool accepts two parameters: \
--targetFolder which specifies the folder where the pictures have to be saved in \
--date which specifies the day when the picture was captured 

For example use this command to save pictures in the folder 'pictures' and the day 2023-12-13: \
download-images --targetFolder pictures --date 2023-12-13
