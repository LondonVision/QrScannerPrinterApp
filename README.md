Helper scripts files are not to be used in the andriod project as some are in python
The helper scripts are fairly self explanatory as to where and how they are to be used but:
One converts a spreadsheet in csv format into individual json files based on row
The next converts those json files into qr codes that hold that data
The last helper script is used in the spreadsheet script that enables the app to send data to the sheet

To find the mainActivity file, go to: app/src/main/java/com/example/qrscanner/MainActivity.java
To use printer plus library, know that the printcom.aar library within the libs folder is resposible for its use
The documentation for printer plus can be found under their gitHub page

The gradle is very important for implenting different libraries so they can be used in the main activity
