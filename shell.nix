{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    babaska
    clojure-lsp
    clj-kondo
    cljfmt
  ];

}
