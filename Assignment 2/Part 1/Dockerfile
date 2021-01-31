FROM haskell:8
RUN cabal update
RUN cabal v2-install --lib HUnit
ENTRYPOINT ["/bin/bash"]