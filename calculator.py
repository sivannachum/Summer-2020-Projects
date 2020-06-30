# Made by following this tutorial:
# https://youtu.be/J-mvqlkHOHY
# but added some of my own code / corrections

# Used this great website to get the colors I wanted!
# https://www.rgbtohex.net/

from tkinter import *
from math import sqrt
from numpy import log

YES = "Yes"
BOTH = "both"

def calc(source, side):
    # Make the background color purple
    store_obj = Frame(source, borderwidth=1, bd=4, bg="#320032")
    store_obj.pack(side=side, expand=YES, fill=BOTH)
    return store_obj

def button(source, side, text, command=None):
    """
    Make buttons on the calculator
    """
    # Make the button green and the text on it white
    store_obj = Button(source, text=text, command=command, bg="#009B00", fg="#F0F0F0")
    store_obj.pack(side=side, expand=YES, fill=BOTH)
    return store_obj

class app(Frame):
    """
    Create the calculator app
    """
    def __init__(self):
        Frame.__init__(self)
        self.option_add("*Font", "monospace 20 bold")
        self.pack(expand=YES, fill=BOTH)
        self.master.title("Calculator")
    
        display = StringVar()
        # Create the area that shows the user's calculations (what they've typed and the answer)
        # Make it have a RIDGE texture (as opposed to SUNKEN, FLAT, etc.)
        # Justify the text to the right, make the background purple and the text white
        # Make the width of the entire calculator 40
        Entry(self, relief=RIDGE, textvariable=display, justify='right', width=30, bd=30, bg="#320032", fg="#F0F0F0").pack(side=TOP, expand=YES, fill=BOTH)

        # Create the clear buttons
        for clear_but in (["CE"], ["C"]):
            erase = calc(self, TOP)
            for char in clear_but:
                button(erase, LEFT, char, lambda store_obj=display, q=char: store_obj.set(''))

        # Create the number and calculation buttons
        for num_but in ("789/", "456*", "123-", "0()+"):
            function_num = calc(self, TOP)
            for char in num_but:
                button(function_num, LEFT, char, lambda store_obj=display, q=char: store_obj.set(store_obj.get() + q))
        
        # Create more functions
        for func_but in ([["|x|", "ln", "mod", "^", "sqrt", "."]]):
            function = calc(self, TOP)
            for func in func_but:
                if func == "|x|":
                    button(function, LEFT, func, lambda store_obj=display, q=func: store_obj.set("abs(" + store_obj.get() + ")"))
                elif func == "ln":
                    button(function, LEFT, func, lambda store_obj=display, q=func: store_obj.set("log(" + store_obj.get() + ")"))
                elif func == "mod":
                    button(function, LEFT, func, lambda store_obj=display, q="%": store_obj.set(store_obj.get() + q))
                elif func == "^":
                    button(function, LEFT, func, lambda store_obj=display, q="**": store_obj.set(store_obj.get() + q))
                elif func == "sqrt":
                    button(function, LEFT, func, lambda store_obj=display, q=func: store_obj.set("sqrt(" + store_obj.get() + ")"))
                else:
                    button(function, LEFT, func, lambda store_obj=display, q=func: store_obj.set(store_obj.get() + q))

        # Make the equals button
        function_equals = calc(self, TOP)
        for equals in "=":
            if equals == "=":
                button_equals = button(function_equals, LEFT, equals)
                button_equals.bind("<ButtonRelease-1>", lambda e, s=self, store_obj=display: s.inner_calc(store_obj), "+")
            else:
                button_equals = button(function_equals, LEFT, equals, lambda store_obj=display, s=" %s "%equals: store_obj.set(store_obj.get()+s))

    def inner_calc(self, display):
        try:
            display.set(eval(display.get()))
        except:
            display.set("ERROR")

if __name__ == '__main__':
    app().mainloop()
