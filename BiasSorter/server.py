from flask import Flask, g, redirect, render_template, url_for, request
import sqlite3
from bias_sorter_functionality import *

app = Flask(__name__)
app.url_map.strict_slashes = False
app.config.from_object(__name__)
app.config.update({
    'SECRET_KEY': 'youshouldmakethissomethinglongandcomplex'
})

@app.teardown_appcontext
def close_db(_err):
    if hasattr(g, 'db'):
        g.db.commit()
        g.db.close()
        del g.db

@app.route("/")
def main():
    return redirect(url_for('content', group_id='test'))

"""
This page shows live results for sorting
"""
@app.route("/results/<group_id>")
def results(group_id: str):
    db = get_db()
    submissions = create_submissions_list(group_id)
    return calculate_net_results(submissions)

@app.route("/content/<group_id>", methods=["GET", "POST"])
def content(group_id: str):

    # Prompt a user to input a username
    if request.method == "GET":
        return render_template("input_user_name.j2")

    elif request.method == "POST":
        user_exists = False
        user = request.form['username']
        db = get_db()

        for row in db.execute("select curr_index from ranks where user == ? AND group_id == ?", \
                            [user, group_id]):
            user_exists = True
        
        submissions = create_submissions_list(group_id)
        
        if user_exists:
            # TODO: Maybe do some sort of prompt if the username exists to make sure it's them 
            # and not just that this username is what they wanted but it happens to be taken.
            # Could do passwords, or could just do a "this username exists, continue if this is you, otherwise go back"
            finished = False
            for row in db.execute("select finished from last_seen_indices where user == ? AND group_id == ?", \
                [user, group_id]):
                if int(row[0]) == 1:
                    finished = True
            # If the user name exists and they are finished sorting, show their results
            if finished:
                return show_result(group_id, user)
        else:
            # If the user does not exist, initialize the "positions" for each element to be sorted
            initialize_comparisons(submissions, group_id, user)
        
        # Get the user sorting!
        return redirect(url_for('sort', group_id=group_id, user=user))

   
@app.route('/sort/<group_id>/<user>', methods=["GET", "POST"])
def sort(group_id: str, user: str):

    if request.method == "GET":
        return show_battle(group_id, user)

    elif request.method == "POST":
        for key in request.form:
            if key.startswith('submit.'):
                identity = key.partition('.')[-1]

        if identity == 'left':
            return sort_list(-1, group_id, user)
        elif identity == 'right':
            return sort_list(1, group_id, user)

if __name__ == '__main__':
    app.run()