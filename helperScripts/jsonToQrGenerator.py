import qrcode
import json
import os

#converts all json files in the json folder to individual QR codes
def json_to_qrcode(jsonFilePath2):
    with open(jsonFilePath2, 'r') as jsonf:
      data = json.load(jsonf)
      #where all the QR codes will end up: destination folder + the name of the file
      imageFilePath = f'path\\{data["First Name"] + " " + data["Last Name"]}.png'
      qr = qrcode.QRCode(version=1, box_size = 15, border = 5)
      qr.add_data(data)
      qr.make(fit=True)
      img = qr.make_image(fill='black', back_color='white')
      img.save(imageFilePath)

#Where all the json files are stored
path_of_the_directory = 'path'

#goes through
for filename in os.listdir(path_of_the_directory):
    f = os.path.join(path_of_the_directory,filename)
    json_to_qrcode(f)
