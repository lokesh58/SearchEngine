public class MyAVLTree<T extends Comparable<T>> {
	class Node {
		T _data;
		Node _left, _right;
		int _height;
		Node(T data) {
			_data = data;
			_left = _right = null;
			_height = 1;
		}
	}

	private Node _root;
	
	MyAVLTree() {
		_root = null;
	}
	
	public void insert(T data) {
		_root = insert(_root, data);
	}

	private int height(Node n) {
		if (n == null) {
			return 0;
		} else {
			return n._height;
		}
	}
	
	private Node rightRotate(Node y) {
		Node x = y._left;
		y._left = x._right;
		x._right = y;
		y._height = 1+Math.max(height(y._left), height(y._right));
		x._height = 1+Math.max(height(x._left), height(x._right));
		return x;
	}

	private Node leftRotate(Node x) {
		Node y = x._right;
		x._right = y._left;
		y._left = x;
		x._height = 1+Math.max(height(x._left), height(x._right));
		y._height = 1+Math.max(height(y._left), height(y._right));
		return y;
	}

	private int getBalance(Node n) {
		if (n == null) {
			return 0;
		} else {
			return height(n._left)-height(n._right);
		}
	}

	private Node insert(Node node, T data) {
		if (node == null) {
			return new Node(data);
		} else {
			if (data.compareTo(node._data) > 0) {
				node._right = insert(node._right, data);
			} else {
				node._left = insert(node._left, data);
			}
			node._height = 1+Math.max(height(node._left), height(node._right));
			int balance = getBalance(node);
			if (balance > 1) {
				if (data.compareTo(node._left._data) <= 0) {
					return rightRotate(node);
				} else {
					node._left = leftRotate(node._left);
					return rightRotate(node);
				}
			} else if (balance < -1) {
				if (data.compareTo(node._right._data) > 0) {
					return leftRotate(node);
				} else {
					node._right = rightRotate(node._right);
					return leftRotate(node);
				}
			} else {
				return node;
			}
		}
	}

	public T find(T key) {
		return find(_root, key);
	}

	private T find(Node node, T key) {
		if (node == null) {
			return null;
		} else {
			int result = key.compareTo(node._data);
			if (result == 0) {
				return node._data;
			} else if (result > 0) {
				return find(node._right, key);
			} else {
				return find(node._left, key);
			}
		}
	}

	public void delete(T key) {
		_root = delete(_root, key);
	}
	
	private Node maxValueNode(Node node) {
		Node curr = node;
		while (curr._right != null) {
			curr = curr._right;
		}
		return curr;
	}

	private Node delete(Node node, T key) {
		if (node == null) {
			return node;
		} else {
			int compareResult = key.compareTo(node._data);
			if (compareResult > 0) {
				node._right = delete(node._right, key);
			} else if (compareResult < 0) {
				node._left = delete(node._left, key);
			} else {
				if ((node._left == null) || (node._right == null)) {
					//1 or 0 child case
					Node temp = null;
					if (node._left == null) {
						temp = node._right;
					} else {
						temp = node._left;
					}

					if (temp == null) {
						//0 child case
						node = null;
					} else {
						//1 child case
						node = temp;
					}
				} else {
					//2 child case
					Node temp = maxValueNode(node._left);//Inorder predecessor is the biggest in the left subtree (for node with a left subtree)
					node._data = temp._data;
					//Delete the inorder predecessor
					node._left = delete(node._left, temp._data);
				}
			}
			if (node == null) {
				//If there was only 1 child that was deleted
				return node;
			} else {
				node._height = 1+Math.max(height(node._left), height(node._right));
				int balance = height(node._left)-height(node._right);
				if (balance > 1) {
					if (getBalance(node._left) > 0) {
						return rightRotate(node);
					} else {
						node._left = leftRotate(node._left);
						return rightRotate(node);
					}
				} else if (balance < -1) {
					if (getBalance(node._right) <= 0) {
						return leftRotate(node);
					} else {
						node._right = rightRotate(node._right);
						return leftRotate(node);
					}
				} else {
					return node;
				}
			}
		}
	}

	public void display() {
		MyLinkedList<Node> list = new MyLinkedList<>();
		list.insertRear(_root);
		while (!list.isEmpty()) {
			Node curr = null;
			try {
				curr = list.deleteFront();
			} catch (Exception e) {}
			if (curr == null) {
				System.out.print("* ");
			} else {
				System.out.print(curr._data+"("+curr._height+") ");
				list.insertRear(curr._left);
				list.insertRear(curr._right);
			}
		}
		System.out.print('\n');
	}

	public static void main(String args[]) {
		MyAVLTree<Integer> t = new MyAVLTree<>();
		t.insert(12);
		t.display();
		t.insert(33);
		t.display();
		t.insert(36);
		t.display();
		t.insert(3);
		t.display();
		t.insert(2);
		t.display();
		t.insert(13);
		t.display();
		t.insert(39);
		t.display();
		t.insert(37);
		t.display();
	}
}
