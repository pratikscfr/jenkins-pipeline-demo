import sys
import argparse

def parse_args():
    """
    Parse Arguments
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('--URL', type=str, default='',
                        help='The REVRPO_URL.')
    return parser.parse_args()



if __name__ == '__main__':
    ARGS = parse_args()
    print("Hello World")
    revProURL = "https://" + ARGS.URL + "/#/account/login"
    print(revProURL)
    assert 5 == 3+2, "Wrong Addition"
