import pandas as pd
import sys


def find_members_by_city(csv_file: str, city_name: str, output_file: str):
  members_df = pd.read_csv(csv_file)
  members_city_df = members_df.loc[lambda df: df['address'].str.contains(', '+city_name)]
  members_city_json = members_city_df.to_json(orient='table', indent=4)
  with open(output_file, 'w') as outfile:
    outfile.write(members_city_json)

if __name__ == '__main__':
  find_members_by_city(sys.argv[1], sys.argv[2], sys.argv[3])