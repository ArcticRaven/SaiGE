# SaiGE
SaiGE is an experiment in using OpenAI to automate an NPC in the game environment fully. \
It currently only uses gpt-3.5-turbo for all Assistants. They run cheap :D
## Commands
`/ai personality`\
This returns the JSON Object "Character" which contains the current construct of the Ai. \
Contains Name, Personality, Language, and Memories\
`/ai recycle`\
This resets the main thread, useful if SaiGE starts to use incorrect response formats or is just being super funky. 
## Roadmap
### Implemented Features
#### Personality Modification
Personality can be updated at runtime by requesting SaiGE to become a character. This must fall within the bounds of OpenAI's usage policies.
### Upcoming Features
#### Personality Recall
Personalities will be saved and loaded for the Ai to quickly recall in the plugin and adopt the persona with its memories. 

#### Memory Implementation
Stores events/notes about users/themselves and recall that information over time. 
