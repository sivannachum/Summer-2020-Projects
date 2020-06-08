'''
This game is incomplete as the tutorial was not very effective.
I could have completed it myself, but I was not very interested in making a tank game to begin with.
'''

import pygame

pygame.init()

x_size: int = 800
y_size: int = 600

game_display = pygame.display.set_mode((x_size, y_size))
pygame.display.set_caption('Tank')

clock = pygame.time.Clock()
fps: int = 20

WHITE = (240, 240, 240)
BLACK = (0, 0, 0)
RED = (155, 0, 0)
D_RED = (110, 0, 0)
GREEN = (0, 155, 0)
D_GREEN = (0, 110, 0)
BLUE = (0, 0, 155)
D_BLUE = (0, 0, 110)
PURPLE = (50, 0, 50)

small_font = pygame.font.SysFont("monospace", 25)
med_font = pygame.font.SysFont("monospace", 35)
large_font = pygame.font.SysFont("palatinolinotype", 80)

main_tank_x: int = int(x_size * 0.9)
main_tank_y: int = int(y_size * 0.7)
tank_width: int = 40
tank_height: int = 20

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


def message_to_button(msg: str, color, button_x: int, button_y: int, button_width: int, button_height: int, text_size = "small"):
    '''
    Add text to a button
    '''
    text_surface, text_rect = text_objects(msg, color, text_size)
    # center of the button
    text_rect.center = (button_x + button_width//2, button_y + button_height//2)
    game_display.blit(text_surface, text_rect)


def button(msg: str, x, y, width, height, active_color, inactive_color, text_size = "small", action = None):
    '''
    Create a button on the screen
    '''
    # Gets the x and y position of the mouse
    cur = pygame.mouse.get_pos()
    # See if the mouse has been clicked
    click = pygame.mouse.get_pressed()
    # click = (0, 0, 0) if no click, (1, 0, 0) if left click, (0, 1, 0) if middle click, (0, 0, 1) if right click

    # Making button areas and functionality
    if x < cur[0] < x + width and y < cur[1] < y + height:
        # Make the button darker if the mouse is on it
        pygame.draw.rect(game_display, active_color, (x, y, width, height))
        # Do something if the button is clicked
        if click[0] == 1 and action != None:
            if action == "play":
                game_loop()
            elif action == "controls":
                controls()
            elif action == "quit":
                pygame.quit()
                quit()
    else:
        pygame.draw.rect(game_display, inactive_color, (x, y, width, height))

    # Add text to the button
    message_to_button(msg, WHITE, x, y, width, height, text_size)


def controls():
    '''
    Let the user know about the game's controls
    '''
    game_controls: bool = True

    while game_controls:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                quit()

        game_display.fill(PURPLE)
        message_to_screen("Controls", GREEN, -120, "large")
        message_to_screen("Fire: Spacebar", WHITE, -50)
        message_to_screen("Move Turret: Up and Down Arrows", WHITE, -10)
        message_to_screen("Move Tank: Left and Right Arrows", WHITE, 30)
        message_to_screen("Pause: p", WHITE, 70)

        # Add back button; unfortunately cannot be done with button function
        msg: str = "Back"
        x: int = 280
        y: int = 470
        width: int = 240
        height: int = 100
        active_color = D_BLUE
        inactive_color = BLUE
        text_size = "medium"

        # Gets the x and y position of the mouse
        cur = pygame.mouse.get_pos()
        # See if the mouse has been clicked
        click = pygame.mouse.get_pressed()

        # Making button area and functionality
        if x < cur[0] < x + width and y < cur[1] < y + height:
            # Make the button darker if the mouse is on it
            pygame.draw.rect(game_display, active_color, (x, y, width, height))
            # Do something if the button is clicked
            if click[0] == 1:
                game_controls = False
        else:
            pygame.draw.rect(game_display, inactive_color, (x, y, width, height))

        # Add text to the button
        message_to_button(msg, WHITE, x, y, width, height, text_size)
    
        pygame.display.update()
        clock.tick(15)


def tank(x: int, y: int):
    # Draw the turret
    pygame.draw.circle(game_display, BLACK, (x, y), tank_height//2)
    pygame.draw.rect(game_display, BLACK, (x-tank_height, y, tank_width, tank_height))


def score(score: int):
    '''
    Display the user's score on the screen
    '''
    text = small_font.render("Score: " + str(score), True, BLACK)
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

        game_display.fill(PURPLE)
        message_to_screen("Welcome to Tanks!", GREEN, -120, "large")
        message_to_screen("The objective is to shoot and destroy", WHITE, -50)
        message_to_screen("the enemy tank before they destroy you.", WHITE, -10)
        message_to_screen("The more enemies you destroy, the harder they get.", WHITE, 30)

        # Add buttons
        button("Play", 20, 470, 240, 100, D_GREEN, GREEN, "medium", action = "play")
        button("Controls", 280, 470, 240, 100, D_BLUE, BLUE, "medium", action = "controls")
        button("Quit", 540, 470, 240, 100, D_RED, RED, "medium", action = "quit")

        pygame.display.update()
        clock.tick(15)


def game_loop():
    '''
    Run the game
    '''
    game_exit = False
    game_over = False

    while not game_exit:
        
        if game_over:
            message_to_screen("Game Over", RED, y_displace = -80, size = "large")
            message_to_screen("Press C to play again or Q to quit", WHITE, y_displace = 30, size = "medium")
            pygame.display.update()

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

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                game_exit = True
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_LEFT:
                    pass
                elif event.key == pygame.K_RIGHT:
                    pass
                elif event.key == pygame.K_UP:
                    pass
                elif event.key == pygame.K_DOWN:
                    pass
                elif event.key == pygame.K_p:
                    pause()

        game_display.fill(WHITE)
        tank(main_tank_x, main_tank_y)
        
        pygame.display.update()
        clock.tick(fps)
    
    pygame.quit()
    quit()


game_intro()