from PIL import Image

im = Image.open('aag.png')
pixels = im.load()

for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 181, 230, 20 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 90, 115, 10
        
im.save('aags.png')

im = Image.open('aag.png')
pixels = im.load()

for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 255, 0, 0 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 128, 0, 0
        
im.save('aar.png')

im = Image.open('aag.png')
pixels = im.load()
for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 255, 128, 128 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 128, 64, 64

im.save('aars.png')
