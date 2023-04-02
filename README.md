# ORQA Maven Plugin

## Summary

This plugin fits into the Maven lifecycle allowing ORQA tasks to be run as phases in the standard lifecycle, generally, but not exclusively, for testing purposes.

## Lifecycle

Currently we don't have any CI process for either the creation of the release artifact, the deployment of documentation to GitHub or the publication to Sonatype's Maven Central. For the moment, then the builds in CI need to be on snapshot versions of the artifacts.

## Building Locally

To build locally without the GPG key you will need to disable the relevant, platform-specific automatically activated profile, e.g. on a Windows machine:

`mvn install -P '!windows'`

## Deployment of the documentation

Currently, the Maven documentation for the plugin is deployed to https://ibboost.github.io/orqa-maven-plugin/.

The plugin used to do this is https://github.com/github/maven-plugins.

Deployment is activated by building with the Maven profile `deploy-site-to-github`.

For this to work, your local Maven settings.xml needs to contain a server with ID `ibbci.github.com` and credentials relating to the OAuth Personal Access Token that is used to deploy. These should be encrypted locally with a master password to prevent appropriation.

Following that, it should be sufficient on a properly configured POM and settings.xml to simply run `mvn site-deploy -P '!windows,deploy-site-to-github'`

## Troubleshooting

### GitHub Site Plugin fails

When running a goal like `mvn site-deploy -P '!windows,deploy-site-to-github'` we see the message:

`[ERROR] Failed to execute goal com.github.github:site-maven-plugin:0.12:site (default) on project orqa-maven-plugin: Error retrieving user info: Not Found (404) -> [Help 1]`

Although this isn't very helpful and the POM had an existing warning saying "<!-- Currently OAuth token approach not working and this our only avenue so disabling -->" I followed the approach mentioned in this ticket https://github.com/github/maven-plugins/issues/63#issuecomment-102610887 to add in the `user:*` permissions against the existing `ibbci` Personal Access Token (classic) and... it worked and deployed the site from my local desktop.
