-- all sqlite tables have magical "rowid" field you don't need to request that's autoincrement.
create table if not exists submissions(
  group_id text not null,
  content text not null
);

/*
Keep track of which elements a user should be looking at at any given time,
what battle they're on or if they're finished sorting, and
the details for calculating what percentage they've sorted already
*/
create table if not exists last_seen_indices(
  user text not null,
  group_id text not null,
  cmp1 integer not null,
  head1 integer not null,
  cmp2 integer not null,
  head2 integer not null,
  curr_index integer not null,
  battle_number integer not null,
  finish_size integer not null,
  total_size integer not null,
  /* 0 = false, 1 = true */
  finished integer not null
);

/*
Keep track of a given element's rank according to a given user
*/
create table if not exists ranks(
  user text not null,
  group_id text not null,
  element_id integer not null,
  curr_cmp integer not null,
  /* This is always one less than curr_rank */
  curr_index integer not null
);