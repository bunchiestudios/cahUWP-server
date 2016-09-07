CREATE SEQUENCE game_id_seq;
CREATE TABLE game (
  id bigint CONSTRAINT mainkey_game PRIMARY KEY DEFAULT nextval('game_id_seq'),
  name varchar(255) UNIQUE
);

CREATE SEQUENCE player_id_seq;
CREATE TABLE player (
  id bigint CONSTRAINT mainkey_player PRIMARY KEY DEFAULT nextval('player_id_seq'),
  name varchar(255) UNIQUE,
  token char(130) NOT NULL,
  game_id bigint NOT NULL REFERENCES game (id)
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
  daisy_chain_card bigint NOT NULL,
  daisy_chain_game bigint NOT NULL,
  status smallint NOT NULL
);
