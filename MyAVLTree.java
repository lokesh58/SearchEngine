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

	private Node insert(Node root, T data) {
		if (root == null) {
			return new Node(data);
		} else {
			if (data.compareTo(root._data) > 0) {
				root._right = insert(root._right, data);
			} else {
				root._left = insert(root._left, data);
			}
			root._height = 1+Math.max(height(root._left), height(root._right));
			int balance = getBalance(root);
			if (balance > 1) {
				if (data.compareTo(root._left._data) <= 0) {
					return rightRotate(root);
				} else {
					root._left = leftRotate(root._left);
					return rightRotate(root);
				}
			} else if (balance < -1) {
				if (data.compareTo(_root._right._data) > 0) {
					return leftRotate(root);
				} else {
					root._right = rightRotate(root._right);
					return leftRotate(root);
				}
			} else {
				return root;
			}
		}
	}

	public T find(T key) {
		return find(_root, key);
	}

	private T find(Node root, T key) {
		if (root == null) {
			return null;
		} else {
			int result = key.compareTo(root._data);
			if (result == 0) {
				return root._data;
			} else if (result > 0) {
				return find(root._right, key);
			} else {
				return find(root._left, key);
			}
		}
	}

	public void delete(T key) {
		_root = delete(_root, key);
	}
	
	private Node maxValueNode(Node root) {
		Node curr = root;
		while (curr._right != null) {
			curr = curr._right;
		}
		return curr;
	}

	private Node delete(Node root, T key) {
		if (root == null) {
			return root;
		} else {
			int compareResult = key.compareTo(root._data);
			if (compareResult > 0) {
				root._right = delete(root._right, key);
			} else if (compareResult < 0) {
				root._left = delete(root._left, key);
			} else {
				if ((root._left == null) || (root._right == null)) {
					//1 or 0 child case
					Node temp = null;
					if (root._left == null) {
						temp = root._right;
					} else {
						temp = root._left;
					}

					if (temp == null) {
						//0 child case
						root = null;
					} else {
						//1 child case
						root = temp;
					}
				} else {
					//2 child case
					Node temp = maxValueNode(root._left);//Inorder predecessor is the biggest in the left subtree (for node with a left subtree)
					root._data = temp._data;
					//Delete the inorder predecessor
					root._left = delete(root._left, temp._data);
				}
			}
			if (root == null) {
				//If there was only 1 child then this will happen
				return root;
			} else {
				root._height = 1+Math.max(height(root._left), height(root._right));
				int balance = height(root._left)-height(root._right);
				if (balance > 1) {
					if (getBalance(root._left) > 0) {
						return rightRotate(root);
					} else {
						root._left = leftRotate(root._left);
						return rightRotate(root);
					}
				} else if (balance < -1) {
					if (getBalance(root._right) <= 0) {
						return leftRotate(root);
					} else {
						root._right = rightRotate(root._right);
						return leftRotate(root);
					}
				} else {
					return root;
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
