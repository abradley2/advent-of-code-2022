let
  pkgs = import <nixpkgs> { };
in
pkgs.mkShell {
  buildInputs = [
    pkgs.jdk11
    pkgs.leiningen
    pkgs.clojure
    pkgs.nodejs
    pkgs.cmake
    pkgs.elmPackages.elm
  ];
}
