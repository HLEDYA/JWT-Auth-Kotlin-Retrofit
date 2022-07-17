const express = require("express");
const app = express();
const jwt = require("jsonwebtoken");
const bodyParser = require("body-parser");

const accessTokenSecret = "secret-key";

const books = [
  {
    title: "Harry Potter and The Sorcerer's Stone",
    author: "J.K. Rowling",
    pages: 309,
    year: 1998,
  },
  {
    title: "Dragons of Eden",
    author: "Carl Sagan",
    pages: 288,
    year: 1977,
  },
  {
    title: "The Dangers of Intelligence",
    author: "Isaac Asimov",
    pages: 216,
    year: 1986,
  },
];

app.use(bodyParser.json());
app.listen(3000, () => {
  console.log("Books Service started on port 3000");
});

const authenticateJWT = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (authHeader) {
    const token = authHeader.split(" ")[1];

    jwt.verify(token, accessTokenSecret, (err, payload) => {
      if (err) {
        console.log("Token cannot be verified. err: " + err);
        return res.sendStatus(403);
      }
      console.log("token verified for req: " + payload.sub);
      //req.user = foundUser;
      next();
    });
  } else {
    console.log("no auth header found!");
    res.sendStatus(401);
  }
};

app.get("/books", authenticateJWT, (req, res) => {
  console.log("books info send");
  res.json(books);
});
