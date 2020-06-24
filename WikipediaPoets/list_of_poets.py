import requests
from bs4 import BeautifulSoup
import csv
import re

def list_of_poets():
    '''
    Crawl through https://en.wikipedia.org/wiki/List_of_poets,
    retrieve the poets listed
    '''
    url = 'https://en.wikipedia.org/wiki/List_of_poets'
    # Getting that html stuff that doesn't tell a human much
    sourceCode = requests.get(url)
    # Text, images, etc of a website
    plainText = sourceCode.text
    # BeautifulSoup needs data formatted in a special way to be able to sift through it easily
    soup = BeautifulSoup(plainText, features="html.parser")

    '''
    We're going to save the extracted info into a tab-delimited CSV.
    We define it up here so we can add the header
    '''
    with open('list_of_poets_tab.csv','a', encoding='utf-8', newline = '') as f:
        writer = csv.DictWriter(f, fieldnames=["URL", "Poet Name", "Birth Year", \
                                "Death Year", "Extra Info"], delimiter="\t")
        writer.writeheader()

        '''
        What we're looking at here:
        <li>
        <a href="/wiki/Jonathan_Aaron" title="Jonathan Aaron">Jonathan Aaron</a>
        (born 1941), American poet
        </li>
        '''
        # Find all the links on the Wikipedia page
        for link in soup.findAll({'li'}):
            link_text = link.text
            # The child contains the actual link/URL
            child = link.findChild()
            if child is not None:
                child_text = child.text
                # If the link text and the child text are the same, this is not a poet
                if link_text != child_text:
                    # Just some clean-up that the previous 'if' does not catch
                    if child_text == 'Z':
                        pass
                    # At this point we've gotten all the poet names
                    elif link_text == 'Greek (Ancient)':
                        break
                    # In this case, we have a poet!
                    else:
                        # Initialize their birth year and death year
                        birth_year = "unknown"
                        death_year = "unknown"

                        # Find the part of the link text that should contain the birth/death year
                        # \d matches a digit
                        regex = '\(.*\d.*\)'
                        match = re.search(regex, link_text)

                        # The link text might just say "(..., living)", in which case there is not a birth/death year
                        if link_text.find('living)') != -1:
                            death_year = 'living'
                        # If we've matched the regex
                        elif match:
                            # Get the text that matched
                            match_text = match.group(0)

                            # Refining the text

                            # Sometimes the parantheses group comes out like this:
                            # (Born as [name info], etc. etc.) (1900-1989)
                            # \D matches non-digits
                            regex2 = '\(\D*\)'
                            match2 = re.search(regex2, match_text)
                            
                            # This part gets rid of the parantheses group described above
                            if match2:
                                index = match_text.index(match2.group(0))
                                match_text = match_text[0:index] + match_text[index+len(match2.group(0)):]

                            # This part gets rid of the first year in year info formatted like this:
                            # (1440 or 1441–1494)
                            if match_text.find(' or ') != -1:
                                match_text = match_text[match_text.index(' or ')+2:]
                            
                            # Getting more info about the text
                            born: bool = False
                            died: bool = False
                            time_born: str = ""
                            time_died: str = ""

                            # If it says born, it will only contain one year
                            if match_text.find('born') != -1:
                                born = True

                            # Same with died... basically. Ex. died 1549/1550,
                            # in which case we just grab the first year written
                            if match_text.find('died') != -1:
                                died = True

                            # Check for AD and BC; they will be case sensitive
                            if match_text.find('BC') != -1 and match_text.find('AD') != -1:
                                time_born = " BC"
                                time_died = " AD"
                            elif match_text.find('BC') != -1:
                                time_born = " BC"
                                time_died = " BC"
                            elif match_text.find('AD') != -1:
                                time_born = " AD"
                                time_died = " AD"

                            i = 0
                            match_length = len(match_text)
                            '''
                            Find the beginning of the first number
                            '''
                            while i < match_length:
                                if match_text[i] in '1234567890':
                                    break
                                else:
                                    i += 1
                            
                            first_index = i
                            '''
                            Find the last number for the first year
                            '''
                            i += 1
                            while i < match_length:
                                if match_text[i] in '1234567890':
                                    i += 1
                                else:
                                    break
                            
                            first_num = match_text[first_index:i]
                            
                            # in the first two cases here, we're done
                            if born:
                                birth_year = first_num + time_born
                            elif died:
                                death_year = first_num + time_died

                            # Otherwise we have to look if there's a st/nd/rd/th in case this is a century
                            # Or we just have to look for the next year
                            else:
                                # This is a century
                                if i+1 != match_length and match_text[i:i+2] in ['st', 'nd', 'rd', 'th']:
                                        #12th century BC is 1200
                                        if time_born == ' BC':
                                            birth_year = first_num + '00'
                                        #12 century AD is 1101
                                        else:
                                            # This might look stupid if it's the 1st century but oh well
                                            first_number = int(first_num)-1
                                            birth_year = str(first_number) + '01'
                                        birth_year = birth_year + time_born
                                # Otherwise look for the next number
                                else:
                                    birth_year = first_num + time_born
                                    # Ex. 1850/70-1900
                                    if match_text[i] == '/':
                                        i += 1
                                        while i < match_length:
                                            if match_text[i] in '1234567890':
                                                i += 1
                                            else:
                                                break

                                    i += 1
                                    '''
                                    Find the beginning of the second number
                                    '''
                                    while i < match_length:
                                        if match_text[i] in '1234567890':
                                            break
                                        else:
                                            i += 1
                                    
                                    second_index = i
                                    '''
                                    Find the last number for the second year
                                    '''
                                    while i < match_length:
                                        if match_text[i] in '1234567890':
                                            i += 1
                                        else:
                                            break
                    
                                    second_num = match_text[second_index:i]
                                    death_year = second_num + time_died
                                    # Please know that this is a nightmare I cannot code around: (fl. 1815–30)
                                    # For example, what if they lived from 100 BC-50 BC; so I cannot just check if the birth year has more digits than the death year
                                                    
                        # Write to the CSV!
                        writer.writerow({"URL": child.get('href'), "Poet Name": child_text, \
                                        "Birth Year": birth_year, "Death Year": death_year, \
                                        "Extra Info": link_text})

    '''
    ORIGINAL
    for link in soup.findAll({'li'}):
        link_text = link.text
        child = link.findChild()
        if child is not None:
            child_text = child.text
            if link_text != child_text:
                if child_text == 'Z':
                    pass
                elif link_text == 'Greek (Ancient)':
                    break
                else:
                    with open('list_of_poets.txt', 'a', newline = '', encoding='utf-8') as f:
                            f.writelines(child.get('href') + " " + link_text + "\n")
    '''

list_of_poets()