from app_convert_csv_to_json import csv_2_json
import filecmp


def test_csv_2_json():
  csv_2_json('test/members_test_1.csv','test/output_members_test.json')
  assert filecmp.cmp('test/output_members_test.json','test/expected_members_test_1.json', shallow=False)


def test_empty_csv_2_json():
  csv_2_json('test/members_test_2.csv','test/output_members_test.json')
  assert filecmp.cmp('test/output_members_test.json','test/expected_members_test_2.json', shallow=False)