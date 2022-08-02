from app_find_memebers_csv_json import find_members_by_city
import filecmp

def test_find_members_by_city_reading():
  find_members_by_city('members.csv', 'Reading', 'test/output_members_by_city_test.json')
  assert filecmp.cmp('test/output_members_by_city_test.json', 'test/expected_members_by_city_test_1.json', shallow=False)

def test_find_members_by_city_newyork():
  find_members_by_city('members.csv', 'New York', 'test/output_members_by_city_test.json')
  assert filecmp.cmp('test/output_members_by_city_test.json', 'test/expected_members_by_city_test_2.json', shallow=False)