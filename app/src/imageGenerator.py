from PIL import Image

name = 'fort' #name of icon


#Green selected
im = Image.open(name + 'g2.png')
pixels = im.load()

for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 181, 230, 20 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 90, 115, 10
        
im.save(name + 'gs.png')

#Green moved
im = Image.open(name + 'g2.png')
pixels = im.load()

for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 34, 177, 76 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 17, 67, 36
        
im.save(name + 'g.png')


#Red default
im = Image.open(name + 'g2.png')
pixels = im.load()

for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 255, 0, 0 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 128, 0, 0
        
im.save(name + 'r2.png')


#Red selected
im = Image.open(name + 'g2.png')
pixels = im.load()
for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 255, 128, 128 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 128, 64, 64

im.save(name + 'rs.png')

#Red moved
im = Image.open(name + 'g2.png')
pixels = im.load()

for i in range(im.size[0]):    # for every col:
    for j in range(im.size[1]):    # For every row
        if (pixels[i,j] == (0,255,0,255)):
            pixels[i,j] = 191, 15, 23 # set the colour accordingly
        elif (pixels[i,j] == (0,128,0,255)):
            pixels[i,j] = 86, 7, 11
        
im.save(name + 'r.png')
