# sbt-actions-docker-release-template

A template that sets up building docker images and publishing to GitHub Container Registry via GitHub Actions.

Implemented using:
- [sbt-github-actions](https://github.com/djspiewak/sbt-github-actions)
- [sbt-native-packager](https://github.com/sbt/sbt-native-packager)
- [sbt-release](https://github.com/sbt/sbt-release)

## Setup

- Enable read and write workflow permissions
  - Settings > Actions > General > Workflow permissions

- Reset version
  - update version.sbt to `0.1-SNAPSHOT`

# Contributing
Feel free to raise issues / pull requests they are appreciated.

I'm always happy to learn better and cleaner ways of doing things! 😄
