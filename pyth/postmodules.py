import base64
import requests

# read image file
def ReadImage(strImageFilename):

    try:
        image = open(strImageFilename, 'rb')
        image_read = image.read()

    except Exception as e:
        print("Error! " + str(e))
        image_read = False

    return image_read

# encode data to base64
def EncodeImage (image_read):

    try:
        image_64_encode = base64.encodebytes(image_read)

    except Exception as e:
        print("Error! " + str(e))
        image_64_encode = False

    return image_64_encode

# post base64 data
def POSTImage(filename, image_64_encode):

    try:
        # post image code
        url = 'http://localhost:8080/requests/base64'
        data = [
                ('fileName', filename),
                ('fileContent', image_64_encode)
               ]
        r = requests.post(url, data=data)
        print(r.text)

        result = True
    except Exceprion as e:
        print("Error! " + str(e))
        result = False

    return result

# post phrase
def POSTPhrase(strPhrase):

    try:
        # post phrase code
        result = True
    except Exception as e:
        print("Error! " + str(e))
        result = False

    return result

# wait for answer from server
def WaitForAnswer(strAnswer):

    try:
        # waiting for answer
        strAnswer = "Server answer"
    except Exception as e:
        print("Error! " + str(e))
        strAnswer = "ERROR"

    return



