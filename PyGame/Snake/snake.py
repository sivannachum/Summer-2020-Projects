# Life-saving Stack Overflow! https://stackoverflow.com/questions/55876467/pip3-is-not-recognized-as-an-internal-or-external-command-operable-program-or
# py -3 -mpip install pygame
import pygame
import time
import random
from typing import List

# initialize all the pygame modules we'll use
# returns a tuple of (successful, unsuccessful) initializations
pygame.init()

# returns a pygame clock object
clock = pygame.time.Clock()

# define colors
WHITE = (240, 240, 240)
PURPLE = (50, 0, 50)
RED = (255, 0, 0)
GREEN = (0, 155, 0)

# load the snake head sprite (game image)
head_img = pygame.image.load('snakehead.png')
# load the food sprites
apple_img = pygame.image.load('apple.png')
chocolate_img = pygame.image.load('chocolate.png')
banana_img = pygame.image.load('banana.png')
cupcake_img = pygame.image.load('cupcake.png')
carrot_img = pygame.image.load('carrot.png')
mouse_img = pygame.image.load('mouse.png')

food = [apple_img, chocolate_img, banana_img, cupcake_img, carrot_img, mouse_img]
food_length: int = len(food)

# Size of the screen
x_size: int = 800
y_size: int = 600
# the size of the snake blocks (NOT the length of the snake!)
snake_size: int = 20
food_size: int = snake_size
# The direction the snake head should be facing
direction: str = "up"

# frames per second
fps: int = 20
# fonts for the game
# SysFont references the system fonts
small_font = pygame.font.SysFont("monospace", 25)
med_font = pygame.font.SysFont("monospace", 35)
large_font = pygame.font.SysFont("palatinolinotype", 80)

# returns canvas object to draw game objects to
# parameters must be a tuple or a list!
game_display = pygame.display.set_mode((x_size, y_size))
# Title of the pygame window
pygame.display.set_caption('Snake')
'''
# Icon of the window
# best size for icon is 32 x 32
pygame.display.set_icon(head_img)
'''

def text_objects(msg: str, color, size: str):
    '''
    Returns a text surface to the user with the given message and color,
    as well as the rectanglar surface itself that contains that text.
    '''
    # True for anti-aliasing
    if size == "small":
        text_surface = small_font.render(msg, True, color)
    elif size == "medium":
        text_surface = med_font.render(msg, True, color)
    elif size == "large":
        text_surface = large_font.render(msg, True, color)
    
    return text_surface, text_surface.get_rect()


def message_to_screen(msg: str, color, y_displace: int = 0, size="small"):
    '''
    Display a message to the user on the screen, centered
    '''
    # returns surface data and rectangle of surface data
    text_surface, text_rect = text_objects(msg, color, size)
    text_rect.center = int(x_size/2), int(y_size/2) + y_displace
    # put the text on the screen, in the given location
    game_display.blit(text_surface, text_rect)


def snake(snake_size: int, snake_list: List[List[int]], snake_length: int):
    '''
    Draw the snake
    '''
    # head_img is originally facing up
    if direction == "right":
        head = pygame.transform.rotate(head_img, 270)
    elif direction == "left":
        head = pygame.transform.rotate(head_img, 90)
    elif direction == "up":
        head = head_img
    else:
        head = pygame.transform.rotate(head_img, 180)
    
    # Draw the rest of the blocks as rectangles
    for x_and_y in snake_list[:-1]:
        # Parameters: surface to draw on, color, coordinates (top left of object, width, height)
        pygame.draw.rect(game_display, GREEN, [x_and_y[0], x_and_y[1], snake_size, snake_size])
        
        # Can be graphics-accelerated; i.e. can do things that makes this quicker for processing compared to draw.rect
        # game_display.fill(RED, rect=[200, 200, 50, 50])
    
    # Draw the snake head from the image we made
    game_display.blit(head, snake_list[-1])
    

def generate_food(snake_list: List[List[int]] = []) -> List[List[int]]:
    '''
    Generates x and y coordinates as well as an image for the food
    '''
    # Python does not have a do-while loop :(
    while True:
        # Subtract food_size to make sure the food is always within the display screen
        # Round to make the food always a multiple of snake_size so the snake aligns with it perfectly
        # Can comment out the /food_size)*food_size) if you don't care about perfect alignment
        rand_food_x: int = int(round(random.randrange(0, x_size-food_size)/food_size)*food_size)
        rand_food_y: int = int(round(random.randrange(0, y_size-food_size)/food_size)*food_size)
        if [rand_food_x, rand_food_y] not in snake_list:
            break
    # Generate a random index into the list of food images
    rand_food_img = random.randrange(0, food_length)

    return [rand_food_x, rand_food_y, rand_food_img]


def score(score: int):
    '''
    Puts the user's score at the top left of the screen
    '''
    text = small_font.render("Score: " + str(score), True, WHITE)
    game_display.blit(text, [10, 10])


def pause():
    '''
    Allow the user to pause the game
    '''
    message_to_screen("Paused", WHITE, -80, size="large")
    message_to_screen("Press C to continue or Q to quit", WHITE, 30)
    pygame.display.update()

    paused = True
    while paused:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                quit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_c:
                    paused = False
                elif event.key == pygame.K_q:
                    pygame.quit()
                    quit()
        clock.tick(5)


# pygame.display.update() - only updates the specific area you tell it to update; no parameters = update the entire surface
# pygame.display.flip() - like a flip book, gives the appearance of motion with slight changes; updates the entire surface all at once

def game_intro():
    '''
    Create and display the game's starting screen
    '''
    intro: bool = True

    while intro:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                quit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_c:
                    intro = False
                if event.key == pygame.K_q:
                    pygame.quit()
                    quit()

        game_display.fill(PURPLE)
        message_to_screen("Welcome to Snake!", GREEN, -120, "large")
        message_to_screen("The objective of the game is to eat the food!", WHITE, -50)
        message_to_screen("The more food you eat, the longer you get.", WHITE, -10)
        message_to_screen("If you run into yourself or the edges, you lose.", WHITE, 30)
        message_to_screen("Press C to play, P to pause, and Q to quit", WHITE, 170)

        pygame.display.update()
        clock.tick(15)


def game_loop():
    '''
    Run the game
    '''
    # allows us to reference AND MODIFY the global variable direction
    global direction
    direction = "up"

    # For exiting the game fully
    game_exit: bool = False
    # For when the player loses
    game_over: bool = False
    # The list of lists of x and y coordinates (for snake parts)
    snake_list = []
    # The length of the snake / the length of the snake_list
    snake_length = 1
    # Leader of the group of blocks in the snake, starting at (300, 300)
    lead_x: int = int(x_size/2)
    lead_y: int = int(y_size/2)
    # How much to change the x and y positions by
    lead_x_change: int = 0
    lead_y_change: int = 0
    
    food_x_y_img: List[List[int]] = generate_food()
    rand_food_x: int = food_x_y_img[0]
    rand_food_y: int = food_x_y_img[1] 
    rand_food_img = food_x_y_img[2]

    while not game_exit:

        # Tell the user the game is over
        if game_over:
            message_to_screen("Game Over", RED, y_displace = -80, size = "large")
            message_to_screen("Press C to play again or Q to quit", WHITE, y_displace = 30, size = "medium")
            pygame.display.update()

        # Listen to their next action
        while game_over:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    game_exit = True
                    game_over = False
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_q:
                        game_exit = True
                        game_over = False
                    elif event.key == pygame.K_c:
                        game_over = False
                        game_loop()

        # What is the user doing in the screen?
        # Where is their mouse?
        # Did they press a certain key?
        # Did they stop pressing that key?
        # Pygame module tells you every event that happens!

        # Events only reflect CHANGE in status
        # Thus key down stays until there is a key up,
        # so if you're holding down a key, it's only reflected once you key up
        for event in pygame.event.get():
            # print(event) - see what events are taking place
            # if the user exed out of the window
            if event.type == pygame.QUIT:
                game_exit = True

            # if they pressed a key or are holding down a key
            if event.type == pygame.KEYDOWN:
                # if it was the left arrow key;
                # if the user is going left, don't let them go right! - makes the game slightly easier but not perfect in practice :(
                if (event.key == pygame.K_LEFT or event.key == pygame.K_a) and not direction == "right":
                    direction = "left"
                    lead_x_change = -snake_size
                    lead_y_change = 0
                elif (event.key == pygame.K_RIGHT or event.key == pygame.K_d) and not direction == "left":
                    direction = "right"
                    lead_x_change = snake_size
                    lead_y_change = 0
                # if it was the up arrow key
                elif (event.key == pygame.K_UP or event.key == pygame.K_w) and not direction == "down":
                    direction = "up"
                    lead_y_change = -snake_size
                    lead_x_change = 0
                elif (event.key == pygame.K_DOWN or event.key == pygame.K_s) and not direction == "up":
                    direction = "down"
                    lead_y_change = snake_size
                    lead_x_change = 0
                elif event.key == pygame.K_p:
                    pause()

        # if the player goes off the screen, they lose
        if lead_x >= x_size or lead_y >= y_size or lead_x < 0 or lead_y < 0:
            game_over = True
        
        lead_x += lead_x_change
        lead_y += lead_y_change
        
        # Fill the background of the game with a certain color
        # Parameters: color, rectangle to fill - if no second parameter, fills the whole surface
        game_display.fill(PURPLE)
        
        # Draw the food!
        # Choose a random image from the food list
        food_img = food[rand_food_img]
        # Draw the snake head from the image we made
        game_display.blit(food_img, [rand_food_x, rand_food_y])
        
        snake_head = []
        snake_head.append(lead_x)
        snake_head.append(lead_y)
        snake_list.append(snake_head)
        # Make sure we're actually changing out blocks after they have moved
        if len(snake_list) > snake_length:
            del(snake_list[0])
        # The snake head is the last element of the snake_list at any given time
        # See if it has run into any other segment of the snake
        for segment in snake_list[:-1]:
            if snake_head == segment:
                game_over = True
        
        # Draw the snake!
        snake(snake_size, snake_list, snake_length)
        # Update the score
        score(snake_length-1)

        pygame.display.update()

        # Simple collision detection for the snake and food (when they are the same size)
        if lead_x == rand_food_x and lead_y == rand_food_y:
            # Generate a new food
            food_x_y_img = generate_food(snake_list)
            rand_food_x = food_x_y_img[0]
            rand_food_y = food_x_y_img[1]
            rand_food_img = food_x_y_img[2]
            # Let the snake get longer
            snake_length += 1

        '''
        # More complex collision detection for the snake and food (when they are different sizes)
            # if the snake x is within the food x from the right or
            # if the snake x is within the food x from the left
        if (lead_x > rand_food_x and lead_x < rand_food_x + food_size) or \
            (lead_x + snake_size > rand_food_x and lead_x + snake_size < rand_food_x + food_size):
                # if the snake y is within the food y from the right or
                # if the snake y is within the food y from the left
            if (lead_y > rand_food_y and lead_y < rand_food_y + food_size) or \
                (lead_y + snake_size > rand_food_y and lead_y + snake_size < rand_food_y + foood_size):
                # Generate a new food
                food_x_y_img = generate_food(snake_list)
                rand_food_x = food_x_y_img[0]
                rand_food_y = food_x_y_img[1]
                rand_food_img = food_x_y_img[2]
                # Let the snake get longer
                snake_length += 1
        '''

        # parameters = fps that we want
        # more frames per second = run the while loop more often = demand more work from the computer
        clock.tick(fps)

    # exit pygame, uninitialize pygame
    pygame.quit()
    # exit out of python
    quit()


game_intro()
game_loop()
