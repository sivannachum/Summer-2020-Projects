import pygame

pygame.init()

# Define colors
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (0, 0, 255)

x_size = 800
y_size = 600

game_display = pygame.display.set_mode((x_size, y_size))

game_display.fill(BLUE)

# Get a pixel from the display
pix = pygame.PixelArray(game_display)
# Change one pixel
pix[10][10] = GREEN

# Cannot accelerate sprites but can accelerate drawing graphics onto the screen
# Draw a line; parameters = where to draw, color, start point (tuple), end point (tuple), width
pygame.draw.line(game_display, RED, (200, 300), (500, 700), 5)
# Draw a circle: parameters = where to draw, color, center coordinates (tuple), width/radius 
pygame.draw.circle(game_display, RED, (50, 50), 100)
# Draw a rectangle: parameters = where to draw, color, top left + width + height (tuple)
pygame.draw.rect(game_display, GREEN, (150, 150, 200, 100))
# Draw a polygon: parameters = where to draw, color, all points in a tuple of tuples
pygame.draw.polygon(game_display, WHITE, ((140, 5), (200, 16), (600, 222), (88, 333), (555, 222)))


while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            quit()
        pygame.display.update()