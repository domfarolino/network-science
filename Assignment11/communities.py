in_file = open("footballNet.txt","r") # louvain.out || footballNet.txt

lines = in_file.readlines()

communities = {} # {community_ID: [list_of_teams]}

for line in lines:
  line_arr = line.split(" ");
  if not line_arr[1] in communities:
    communities[line_arr[1]] = [line_arr[0]];
  else:
    communities[line_arr[1]].append(line_arr[0])

community_lists = []

for key in communities.keys():
  community_lists.append(communities[key])

community_lists.sort(key=lambda x: x[0])

for community in community_lists:
  for team in community:
    print(team)
  print("")

in_file.close()
