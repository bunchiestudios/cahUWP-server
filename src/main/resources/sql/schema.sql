CREATE SEQUENCE IF NOT EXISTS player_id_seq;
CREATE TABLE IF NOT EXISTS player (
  id bigint CONSTRAINT mainkey PRIMARY KEY DEFAULT nextval('player_id_seq'),
  name varchar(255) UNIQUE,
  token char(130) NOT NULL,
  game_id bigint NOT NULL REFERENCES game (id)
);

CREATE SEQUENCE IF NOT EXISTS game_id_seq;
CREATE TABLE IF NOT EXISTS game (
  id bigint CONSTRAINT mainkey PRIMARY KEY DEFAULT nextval('game_id_seq'),
  name varchar(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS game_cards (
  card_id bigint NOT NULL REFERENCES cards (id),
  game_id bigint NOT NULL REFERENCES game (id),
  daisy_chain_card bigint NOT NULL,
  daisy_chain_game bigint NOT NULL,
  status smallint NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS card_id_seq;
CREATE TABLE IF NOT EXISTS cards (
  id bigint CONSTRAINT mainkey PRIMARY KEY DEFAULT nextval('card_id_seq'),
  message text NOT NULL,
  pick_n smallint NOT NULL,
  black boolean NOT NULL
);