import argparse
import json
from datetime import datetime

parser = argparse.ArgumentParser(description='Simple Task Tracker CLI')

parser.add_argument('operation', choices=['add', 'update', 'delete', 'list'], help='Commands')

parser.add_argument('task_desc', type=str, help='Task description.')

args = parser.parse_args()

if args.operation == 'add':
    timenow = datetime.now().isoformat()
    data = {
        'id': None,
        'description': args.task_desc,
        'status':'todo',
        'createdAt': timenow,
        'updatedAt': timenow
    }
    with open('todo.json', 'w') as f:
        json.dump(data, f, indent=4)
elif args.operation == 'update':
    result = "WIP"
elif args.operation == 'delete':
    result = "WIP"
elif args.operation == 'list':
    result = "WIP"

