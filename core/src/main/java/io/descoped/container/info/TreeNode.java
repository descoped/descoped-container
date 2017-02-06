package io.descoped.container.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oranheim on 05/02/2017.
 */
public class TreeNode<T> {

    private static final Logger log = LoggerFactory.getLogger(TreeNode.class);

    private TreeNode<T> parent;
    private T data;
    private List<TreeNode<T>> children = new ArrayList<>();

    public TreeNode() {
        parent = null;
        data = null;
    }

    public TreeNode(TreeNode<T> parent, T data) {
        this.parent = parent;
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public T getData() {
        return data;
    }

    public int getLevel() {
        int depth = 0;
        TreeNode<T> current = this;
        while(current.getParent() != null) {
            current = current.getParent();
            depth++;
        }
        return depth;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return (data != null) && !isGroup();
    }

    public boolean isGroup() {
        return children.size() > 0;
    }

    public TreeNode<T> add(T data) {
        TreeNode<T> child = new TreeNode<T>(this, data);
        children.add(child);
        return child;
    }

}
