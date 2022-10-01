CREATE TABLE notes (
  id         SERIAL PRIMARY KEY,
  login      VARCHAR(20),
  date       DATE,
  note       TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
