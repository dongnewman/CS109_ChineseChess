# xiangqi-assets (processed)

This branch contains the xiangqi asset SVGs and a helper script to convert text into paths and center the glyphs.

Contents:
- board.svg — chessboard SVG
- piece-*.svg — 14 chess piece SVGs (text-based, visually centered)
- convert-and-center.js — Node script that calls Inkscape to convert text to paths and recenters the resulting paths; it outputs processed SVGs to ./out and creates xiangqi-assets-paths.zip

Notes:
- The piece SVGs in this commit still contain text elements (not yet converted to path). Use the included convert-and-center.js with Inkscape to produce path-based SVGs that are guaranteed to render identically on all platforms.
- To run the conversion script locally:
  1. Install Node.js and Inkscape 1.0+ and ensure `inkscape` is in your PATH.
  2. Install Node deps: `npm install xmldom svg-path-bounds archiver`
  3. Place the SVG files in the same directory as the script and run: `node convert-and-center.js`

License:
- These assets were generated and are intended to be released under CC0 / Public Domain. If you want a different license, update accordingly.
