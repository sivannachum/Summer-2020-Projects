from server import connect_db

tuples = [
        ("test", "alpha"),
        ("test", "beta"),
        ("test", "gamma"),
        ("test", "theta"),
        ("test", "epsilon"),
        ];

if __name__ == '__main__':
    with open('schema.sql') as fp:
        setup = fp.read()
    with connect_db() as db:
        db.executescript(setup)
        db.executemany("""insert into submissions(group_id, content) values (?,?)""", tuples)

