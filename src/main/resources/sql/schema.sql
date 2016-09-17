CREATE SEQUENCE game_id_seq;
CREATE TABLE game (
  id bigint CONSTRAINT mainkey_game PRIMARY KEY DEFAULT nextval('game_id_seq'),
  name varchar(255) UNIQUE NOT NULL,
  black_deck bigint REFERENCES game_cards (card_id),
  white_deck bigint REFERENCES game_cards (card_id)
);

CREATE SEQUENCE player_id_seq;
CREATE TABLE player (
  id bigint CONSTRAINT mainkey_player PRIMARY KEY DEFAULT nextval('player_id_seq'),
  name varchar(255) UNIQUE,
  token char(130) NOT NULL,
  game_id bigint REFERENCES game (id)
);

CREATE SEQUENCE card_id_seq;
CREATE TABLE cards (
  id bigint CONSTRAINT mainkey_cards PRIMARY KEY DEFAULT nextval('card_id_seq'),
  message text NOT NULL,
  pick_n smallint NOT NULL,
  black boolean NOT NULL
);

CREATE TABLE game_cards (
  card_id bigint NOT NULL REFERENCES cards (id),
  game_id bigint NOT NULL REFERENCES game (id),
  player_id bigint REFERENCES player (id),                 -- Playing inferred by whether this is null
  daisy_chain bigint REFERENCES game_cards (card_id),      -- Used to define the deck
  status smallint NOT NULL                                 -- First bit: played
);
