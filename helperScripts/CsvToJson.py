import csv
import json 
 
# Function to convert a CSV to JSON
# Takes the file paths as arguments
def make_json(csvFilePath):
     
    # create an empty list
    data = []
     
    # Open a csv reader called DictReader
    with open(csvFilePath, encoding='utf-8') as csvf:
        csvReader = csv.DictReader(csvf)
         
        # Convert each row into a dictionary (can easily convert dicts into json)
        # and add it to data
        for row in csvReader:             
            data.append(row)
 
    # Go through data list
    # Open a json writer, and use the json.dumps()
    # function to dump data
    for item in data:
      #where you want the json files to end up
        jsonFilePath = f'path\\{item["""First Name"""] + " " + item["""Last Name"""]}.json'
        with open(jsonFilePath, 'w', encoding='utf-8') as jsonf:
            jsonf.write(json.dumps(item))
         

# This path should lead directly to the csv your are trying to extract
csvFilePath = f'path'

# Call the make_json function
make_json(csvFilePath)
