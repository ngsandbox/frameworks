import postmodules

while True:

    print("Please select <I>mage, <P>hrase or <B>reak: ")
    sChoice = input()

    # do for Image option selected
    if sChoice in ('I', 'i', 'Ш', 'ш'):
        # ask for filename with image
        print("Please enter image filename: ")
        strFileName = input()

        # read file
        image_read = postmodules.ReadImage(strFileName)
        if image_read == False:
            break

        # encode file
        image_64_encode = postmodules.EncodeImage(image_read)
        if image_64_encode == False:
            break

        # send image data to server and wait for response
        print ("\nPOST image %s", strFileName)
        post_result = postmodules.POSTImage(strFileName, image_64_encode)

        if post_result == False:
            print("\nError. Did not received response")
            break

        # wait for server answer
        strAnswer = ""
        postmodules.WaitForAnswer(strAnswer)
        print(strAnswer)

    # do for Phrase option selected
    elif sChoice in ('P', 'p', 'З', 'з'):
        # ask for phrase
        print("Please enter phrase: ")
        strPhrase = input()

        # send phrase to server
        if POSTPhrase(strPhrase) == false:
            print("\nError. Can not send phrase")
            break

        # wait for response and print result
        print("Answer: %s", WaitForAnswer())

    # do for Break
    elif sChoice in ('B', 'b', 'И', 'и'):
        break

    # do for other
    else:
        print("\nIncorrect input value. Try again\n")
