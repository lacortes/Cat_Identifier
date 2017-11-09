# Cat Pokedex
***

## Notes on Git
***
These are some helpful steps and commands that you will be using as part of collaborating to the repo.

### Don't forget to Pull from master to have latest changes
  1. Checkout master branch: ```git checkout master```
 
  2. Pull from origin/master: ```git pull```
  
### Creating a branch for implementing a feature/update
  1. Perform the above steps if you have not done so yet.

  2. Branch off master: ``git branch <sandbox/user_name/name_of_branch>``
    * **FOLLOW THIS CONVENTION**: sandbox/user_name/name_of_branch

  3. You can verify branch creation with this command: ```git branch```. You will see a list with the name of the newly created branch.
 
  4. **Don't forget**, *checkout* the branch to begin working in it: ```git checkout <sandbox/user_name/name_of_branch>```
  
  5. Lastly, begin working in your new branch

### Pushing your changes to the master repo so everyone can enjoy new features
  1. Make sure that all changes you want to commit are staged: ```git add [.] [<path_to_file>]```
  
  2. Next, commit the stages files: ```git commit -m "Meaningful explanation of changes."``` 

  3. Push local branch to master repo (Github): ```git push```
  
  4. You might receive an error that looks similar to: ```git branch --set-upstream my_branch origin/my_branch```
    * Just copy the command and run it. The push should then work.

  5. **ONE MORE THING**. Sign in to your GitHub account via web browser, go to the repository, and you should see a message saying to submit **New Pull Request**.
    * Click on **New Pull Request**, assign everyone to review. 
    * The branch will be fully merged once the team has reviewed the code.   
