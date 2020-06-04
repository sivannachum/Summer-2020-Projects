'''
This script converts our Snake game to an executable
To run: py -3 setup.py build #makes a folder you can zip and send to friends
py -3 setup.py bdist_msi #creates an installer
'''

import cx_Freeze

# The scripts we want cx_Freeze to convert to an executable
executables = [cx_Freeze.Executable("snake.py")]

# All the setup commands that we want:
# Name of application
# Options = packages we're using, files we need (in this case, image files)
cx_Freeze.setup(
    name="Snake",
    options={"build_exe":{"packages":["pygame", "time", "random", "typing"],
    "include_files":["snakehead.png", "apple.png", "chocolate.png", "banana.png", "cupcake.png", "carrot.png", "mouse.png"]}},
    description = "Snake Game",
    executables = executables
    )