import requests
from bs4 import BeautifulSoup
import csv

def jeopardy(page: int = 1):
    # Change the page we're looking at each time
    url = 'http://www.j-archive.com/showgame.php?game_id='
    url = url + str(page)
    # Getting that html stuff that doesn't tell a human much
    sourceCode = requests.get(url)
    # Text, images, etc of a website
    plainText = sourceCode.text
    # BeautifulSoup needs data formatted in a special way to be able to sift through it easily
    soup = BeautifulSoup(plainText, features="html.parser")

    # Reset the lists each time
    questions_answers: dict = {}
    categories: list = []

    # Get show information
    '''
    What we're looking at here:
    <h1>Show #4596 - Monday, September 6, 2004</h1>
    '''
    show_title = ""
    for text in soup.findAll({'h1'}):
        game = text.string
        if game is not None:
            show_title = game

    # Get the categories for this game
    '''
    What we're looking at here:
    <td class="category_name">HISTORY</td>
    '''
    for text in soup.findAll({'td'}):
        category = text.get('class')
        if text.get('class') is not None and category[0] == 'category_name':
            categories.append(text.string)

    show_comments = ""
    # Get the answers to the questions + any show comments
    for text in soup.findAll({'div'}):
        answer = text.get('onmouseover')
        '''
        What we're looking at here:
        <div onmouseover="toggle('clue_J_1_3', 'clue_J_1_3_stuck', 
        '<em class="correct_response">Abraham</em><br />
        '''
        if answer is not None:
            # Remove the beginning "toggle('" part of the string
            remove_toggle = answer[8::]
            # Get the clue_id, ex. clue_J_1_3
            clue_id = remove_toggle[0:remove_toggle.index("'")]
            # Get the correct response
            remove_correct_response = remove_toggle[remove_toggle.index('<em')::]
            correct_response = remove_correct_response[\
                remove_correct_response.index('>')+1:remove_correct_response.index('</em>')]
            # Map the clue_id to the correct_response
            questions_answers[clue_id] = correct_response
            
        # Get any game comments, if there are any, ex. "1989 College Championship quarterfinal game 5."
        '''
        What we're looking at here:
        <div id="game_comments">1989 College Championship quarterfinal game 5.</div>
        '''
        identifier = text.get('id')
        if identifier =='game_comments':
            text_str = text.string
            if text_str is not None:
                show_comments = text_str


    # Category names are separate because we want to have them all before we start matching
    # questions, answers, and categories 
    for text in soup.findAll({'td'}):
        identifier = text.get('id')
        if identifier is not None and identifier in questions_answers:
            text_str = text.string
            if text_str is not None:
                # Now we have the question with its corresponding answer
                question = text_str
                answer = questions_answers.pop(identifier)
                category: str = ""
                amount: int = 1000
                which_round: str = "Final Jeopardy Round"

                # Jeopardy clue
                if identifier.startswith("clue_J"):
                    '''
                    Example clue_id: clue_J_2_1
                    This is in the second column and first row,
                    so it's category will be element 1 in the categories array
                    based on how the HTML is ordered on the jeopardy archive page.
                    Also, since this is Jeopardy, it's value will be 1 * 200 = $200
                    '''
                    category = categories[int(identifier[7])-1]
                    amount = int(identifier[-1])*200
                    which_round = "Jeopardy Round"
                # Double Jeopardy clue
                elif identifier.startswith("clue_DJ"):
                    category = categories[int(identifier[8])+5]
                    amount = int(identifier[-1])*400
                    which_round = "Double Jeopardy Round"
                # Final Jeopardy clue
                else:
                    category = categories[-1]

                delimiter: str = "{+~}"
                # Write straight to the csv from here!
                if (category is not None):
                    try:
                        with open('jeopardy.txt', 'a', newline = '') as f:
                            f.writelines(show_title + delimiter + show_comments + delimiter \
                                        + which_round + delimiter + category
                                        + delimiter + str(amount) + delimiter + question + delimiter + answer + "\n")
                    except UnicodeEncodeError as identifier:
                        pass

                '''
                with open('jeopardy.csv','a', encoding='utf-8', newline = '') as f:
                    writer = csv.DictWriter(f, fieldnames=["Show", "Comments", "Round", "Category", \
                        "Amount", "Question", "Answer"], delimiter='~')
                    writer.writerow({"Show": show_title, "Comments": show_comments, \
                                    "Round": which_round, "Category": category, \
                                    "Amount": amount, "Question": question, "Answer": answer})
                '''  

# 6665 pages total
for i in range (243, 6000):
    jeopardy(i)