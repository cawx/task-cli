import argparse
import json
from datetime import datetime

def add_task(task):
    timenow = datetime.now().isoformat()
    data = {
        'id': 1,
        'description': task,
        'status':'todo',
        'createdAt': timenow,
        'updatedAt': timenow
    }
    with open('todo.json', 'w') as f:
        json.dump(data, f, indent=4)
        print("Task successfully added.")

def delete_task(task_id):
    with open('todo.json', 'r') as f:
        data = json.load(f)
    tasks = [task for task in data if task['id'] != task_id]
    with open('todo.json', 'w') as f:
        json.dump(tasks, f, indent=4)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Simple Task Tracker CLI')
    parser.add_argument('operation', choices=['add', 'update', 'delete', 'list'], help='Commands')
    parser.add_argument('--task_desc', type=str, help='Task description.')
    parser.add_argument('--task_id', type=int, help='Task id.')
    args = parser.parse_args()

    # ADD A NEW TASK
    if args.operation == 'add':
        add_task(args.task_desc)
    # UPDATE A TASK
    elif args.operation == 'update':
        result = "WIP"
    #DELETE A TASK
    elif args.operation == 'delete':
        delete_task(args.task_id)
    # LIST ALL TASKS
    elif args.operation == 'list':
        result = "WIP"