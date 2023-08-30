import sqlite3
from flask import g

def connect_db(dbpath='file.db'):
    rv = sqlite3.connect(dbpath, timeout=5, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    rv.row_factory = sqlite3.Row
    return rv

def get_db():
    if not hasattr(g, 'db'):
        g.db = connect_db()
    return g.db