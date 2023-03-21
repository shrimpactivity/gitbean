# GitBean
A basic implementation of the Git VCS in Java. This is a personal project I undertook based on a similar design spec outlined in [UC Berkeley's CS61B course](https://sp21.datastructur.es/). I constructed my own utility and testing packages to avoid using course project boilerplate. 

GitBean supports basic file version control, but at the moment cannot handle files located in sub-directories of the repository. It is also impossible to be in a detached-HEAD state in GitBean, unlike the real Git. 

I don't expect anyone to download and run a random jar off the internet designed to manipulate their files, but be my guest!

# How To Use
- Download [gitbean.jar](https://github.com/shrimpactivity/gitbean/blob/master/gitbean.jar?raw=true)
- Open a terminal and navigate to the desired directory for your gitbean repository.
- Run `java -jar <PATH_TO_GITBEAN.JAR> init` to create a repository.

# Commands
Preface all commands with `java -jar <PATH_TO_GITBEAN.JAR>`
- `init` Create a new gitbean repo in the current directory.
- `add <FILE_NAME>` Stage the specified file.
- `commit <MESSAGE>` Commits the staged files with the provided commit message. 
- `rm <FILE_NAME>` Un-stages a file and removes it from the repository.
- `log` The commit history of the current branch.
- `global-log` Info of all repository commits. 
- `find <MESSAGE>` Gets the IDs of any commits with the provided message.
- `status` Repository status, included branches, modifications, and untracked changes.

# Commands In Progress
- `checkout`
- `branch`
- `rm-branch`
- `merge`
- `reset`
