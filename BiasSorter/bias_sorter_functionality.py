from math import ceil, floor
from typing import List, Tuple, Set, Dict
from flask import render_template
from db_connect import *
import random

""" This method is mainly be used in overall server setup """
def create_submissions_list(group_id: str) -> List[int]:
    """
    Get the DB ids for the elements with group_id
    """
    db = get_db()
    submissions = []
    for row in db.execute("select rowid from submissions where group_id == ?", [group_id]):
        submissions.append(row[0])
    return submissions

""" These methods are used for each user's individual experience """
def initialize_comparisons(submissions: List[int], group_id: str, user: str):
    total_size = 0
    finish_size = 0
    # Start on question number 1
    battle_number = 1

    n = 0
    random.shuffle(submissions)

    # The sequence that the user should sort
    # Set the first element to an array
    indices_of_members = []
    indices_of_members.append([])

    db = get_db()
    db.execute("BEGIN")
    # Populate that element with the element ids to be sorted
    for i in range(len(submissions)):
        indices_of_members[n].append(submissions[i])
        # Write to the DB the index each element is at at this point in time for recovery purposes
        db.execute("insert into ranks(user, group_id, element_id, curr_cmp, curr_index) values (?, ?, ?, ?, ?)",\
                (user, group_id, submissions[i], n, i))

    n += 1
    mid = 0

    i: int = 0
    while i < len(indices_of_members):
        # Divide the list found at this index in half, if possible
        """
        This will create lists like:
        0: 0 1 2 3 4 5 6
        1: 0 1 2 3
        2: 4 5 6
        3: 0 1
        4: 2 3
        5: 4 5
        6: 6
        7: 0
        8: 1
        9: 2
        10: 3
        11: 4
        12: 5
        """
        if len(indices_of_members[i]) >= 2:
            # Find the midpoint of the element in list member    
            mid = ceil(len(indices_of_members[i])/2)
            # Create a new array at the next n
            indices_of_members.append([])
            # Make it be the first half of the original list member
            indices_of_members[n] = indices_of_members[i][0:mid]
            # Keep track of the total size thus far
            total_size += len(indices_of_members[n])
            for j in range(len(indices_of_members[n])):
                # Write to the DB the index each element is at for comparisons in sorting later
                db.execute("update ranks set curr_cmp = ?, curr_index = ? where user == ? AND element_id == ?",\
                        (n, j, user, indices_of_members[n][j]))
            n += 1

            # Create a new array at the next n
            indices_of_members.append([])
            # Make it be the second half of the list
            indices_of_members[n] = indices_of_members[i][mid:len(indices_of_members[i])]
            # Increase the total size
            total_size += len(indices_of_members[n])
            for j in range(len(indices_of_members[n])):
                # Write to the DB the index each element is at for comparisons in sorting later
                db.execute("update ranks set curr_cmp = ?, curr_index = ? where user == ? AND element_id == ?",\
                        (n, j, user, indices_of_members[n][j]))
            n += 1
        
        i += 1

    # Start from the back of the comparisons list (indices_of_members) and go forward
    cmp1 = len(indices_of_members)-2
    cmp2 = len(indices_of_members)-1
    head1 = 0
    head2 = 0

    db.execute("insert into last_seen_indices(user, group_id, cmp1, head1, cmp2, head2, curr_index, battle_number, finish_size, total_size, finished) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", \
                (user, group_id, cmp1, head1, cmp2, head2, 0, battle_number, finish_size, total_size, 0))
    db.execute("COMMIT")

def sort_list(flag: int, group_id: str, user: str):
    """Reacts to a user's sorting decision and sorts the list accordingly"""
    i: int = 0
    db = get_db()

    db.execute("BEGIN")
    chosen = db.execute("select cmp1, head1, cmp2, head2, curr_index, battle_number, finish_size from last_seen_indices where user == ? AND group_id == ?",\
            [user, group_id]).fetchone()
    cmp1 = chosen[0]
    head1 = chosen[1]
    cmp2 = chosen[2]
    head2 = chosen[3]
    # The current index is the new ranking / curr_index for the selected element
    chosen_index = chosen[4]
    battle_number = chosen[5]
    finish_size = chosen[6]
    # The parent of this element will be its new curr_cmp
    parent = cmp1 // 2

    finished_left_list = False
    finished_right_list = False

    # If they select the left option
    if flag < 0:
        # Get the chosen element's ID!
        chosen_id = db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
            [user, group_id, cmp1, head1]).fetchone()[0]
        
        # Update the element's position (curr_index also indicates its new ranking implicitly)
        db.execute("update ranks set curr_cmp = ?, curr_index = ? where user == ? AND element_id == ?",\
            (parent, chosen_index, user, chosen_id))
        
        head1 += 1

        finished_left_list = True
        for row in db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
            [user, group_id, cmp1, head1]):
            finished_left_list = False
    
    # If they select right option
    elif flag > 0:
        # Get the chosen element's ID!
        chosen_id = db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
            [user, group_id, cmp2, head2]).fetchone()[0]
        
        # Update the element's position (curr_index also indicates its new ranking implicitly)
        db.execute("update ranks set curr_cmp = ?, curr_index = ? where user == ? AND element_id == ?",\
            (parent, chosen_index, user, chosen_id))
        
        head2 += 1
        finished_right_list = True
        for row in db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
            [user, group_id, cmp2, head2]):
            finished_right_list = False
    
    chosen_index += 1
    finish_size += 1

    # Processing after finishing with one list
    # If done with left list but not the right list
    if finished_left_list:
        # Put the remaining list items in their proper index / rank
        while not finished_right_list:
            to_edit = db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
                [user, group_id, cmp2, head2]).fetchone()
            if to_edit:
                remaining_id = to_edit[0]
                db.execute("update ranks set curr_cmp = ?, curr_index = ? where user == ? AND element_id == ?",\
                    (parent, chosen_index, user, remaining_id))
                head2 += 1
                chosen_index += 1
                finish_size += 1
            else:
                finished_right_list = True
                
    # If done with the right list but not the left list
    elif finished_right_list:
        # Put the remaining list items in their proper index / rank
        while not finished_left_list:
            to_edit = db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
                [user, group_id, cmp1, head1]).fetchone()
            if to_edit:
                remaining_id = to_edit[0]
                db.execute("update ranks set curr_cmp = ?, curr_index = ? where user == ? AND element_id == ?",\
                    (parent, chosen_index, user, remaining_id))
                head1 += 1
                chosen_index += 1
                finish_size += 1
            else:
                finished_left_list = True
                
    # When it arrives at the end of both lists
    if finished_left_list and finished_right_list: 
        # Go to the next list pairs to sort / compare
        cmp1 = cmp1 - 2
        cmp2 = cmp2 - 2
        
        # Reset head1 and head2 to 0
        head1 = 0
        head2 = 0

        # Reset the chosen index
        chosen_index = 0

    # In this case, we've sorted all the lists
    if cmp1 < 0:
        # Indicate that sorting has finished
        db.execute("update last_seen_indices set cmp1 = ?, finish_size = ?, finished = ? where user == ? AND group_id == ?",\
            (-1, finish_size, 1, user, group_id))
        db.execute("COMMIT")
        # Show the result of the sorting
        return show_result(group_id, user)
    
    # Otherwise, show the next battle
    else:
        # Increase the question number
        battle_number += 1
        # Write to the last_seen_indices DB
        db.execute("update last_seen_indices set cmp1 = ?, head1 = ?, cmp2 = ?, head2 = ?, curr_index = ?, battle_number = ?, finish_size = ? where user == ? AND group_id == ?",\
            (cmp1, head1, cmp2, head2, chosen_index, battle_number, finish_size, user, group_id))
        db.execute("COMMIT")
        return show_battle(group_id, user)

# The results
def show_result(group_id: str, user: str):
    db = get_db()
    string = ""
     
    string += "<table style=\"width:800px; font-size:18px; line-height:120%; margin-left:auto; margin-right:auto; border:1px solid #000; border-collapse:collapse\" align=\"center\">"
    string += "<tr><td style=\"color:#ffffff; background-color:#e097d9; text-align:center;\">rank</td><td style=\"color:#ffffff; background-color:#e097d9; text-align:center;\">options</td></tr>"

    for row in db.execute("select curr_index, element_id from ranks where user == ? AND group_id == ? order by curr_index",[user, group_id]):
        string += "<tr><td style=\"border:1px solid #000; text-align:center; padding-right:5px;\">"+ \
                str(row[0]+1)+"</td><td style=\"border:1px solid #000; padding-left:5px;\">"+ \
                db.execute("select content from submissions where rowid == ?", [row[1]]).fetchone()[0] +"</td></tr>"

    string += "</table>"

    finish_size = 0
    total_size = 0
    for row in db.execute("select finish_size, total_size from last_seen_indices where user == ? AND group_id == ?", \
        [user, group_id]):
        finish_size = row[0]
        total_size = row[1]

    return render_template('battle_results.j2', \
            percent_sorted=floor(finish_size*100/total_size), \
            results=string
            )

# Indicates two elements to compare
def show_battle(group_id: str, user: str):
    # Set the battle number and comparisons to be made
    db = get_db()
    battle = db.execute("select cmp1, head1, cmp2, head2, battle_number, finish_size, total_size from last_seen_indices where user == ? AND group_id == ?",\
            [user, group_id]).fetchone()
    cmp1 = battle[0]
    head1 = battle[1]
    cmp2 = battle[2]
    head2 = battle[3]
    battle_number = battle[4]
    finish_size = battle[5]
    total_size = battle[6]

    left_id = db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
            [user, group_id, cmp1, head1]).fetchone()[0]
    right_id = db.execute("select element_id from ranks where user == ? AND group_id == ? AND curr_cmp == ? AND curr_index == ?",\
            [user, group_id, cmp2, head2]).fetchone()[0]

    left_option = db.execute("select content from submissions where rowid == ?", [left_id]).fetchone()[0]
    right_option = db.execute("select content from submissions where rowid == ?", [right_id]).fetchone()[0]

    return render_template('battle_display.j2', \
            battle_number=battle_number, percent_sorted=floor(finish_size*100/total_size), \
            left_option=left_option, \
            right_option=right_option
            )

# Shows the overall results for all users who are sorting / have sorted
def calculate_net_results(submissions: List[int]):
    db = get_db()
    
    choice_to_points = {}
    max_points = len(submissions)

    for choice in submissions:
        for row in db.execute("select curr_index from ranks where element_id == ?", [choice]):
            choice_to_points[choice] = choice_to_points.get(choice, 0) + max_points-row[0]
    
    string = ""
     
    string += "<table style=\"width:800px; font-size:18px; line-height:120%; margin-left:auto; margin-right:auto; border:1px solid #000; border-collapse:collapse\" align=\"center\">"
    string += "<tr><td style=\"color:#ffffff; background-color:#e097d9; text-align:center;\">points</td><td style=\"color:#ffffff; background-color:#e097d9; text-align:center;\">options</td></tr>"

    # Sort by points from highest to lowest
    for choice, points in sorted(choice_to_points.items(), key=lambda x: x[1], reverse=True):
        string += "<tr><td style=\"border:1px solid #000; text-align:center; padding-right:5px;\">"+ \
                str(points)+"</td><td style=\"border:1px solid #000; padding-left:5px;\">"+ \
                db.execute("select content from submissions where rowid == ?", [choice]).fetchone()[0] +"</td></tr>"

    string += "</table>"
        
    return render_template('overall_results.j2', results=string)