import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TodoGUI {
    private JFrame frame;
    private JTextField taskInput;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JProgressBar progressBar;
    private JLabel progressLabel; 

    public TodoGUI() {
        frame = new JFrame("Todo List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        frame.getContentPane().setBackground(new Color(240, 240, 240));

        taskInput = new JTextField("Enter task here...");
        taskInput.setForeground(Color.GRAY);
        taskInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (taskInput.getText().equals("Enter task here...")) {
                    taskInput.setText("");
                    taskInput.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (taskInput.getText().isEmpty()) {
                    taskInput.setText("Enter task here...");
                    taskInput.setForeground(Color.GRAY);
                }
            }
        });
        frame.add(taskInput, BorderLayout.NORTH);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); // Show percentage
        frame.add(progressBar, BorderLayout.SOUTH);

        progressLabel = new JLabel("Progress: 0%");
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(progressLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        JButton clearButton = new JButton("Clear All");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        frame.add(buttonPanel, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            String taskText = taskInput.getText();
            if (!taskText.isEmpty() && !taskText.equals("Enter task here...")) {
                taskListModel.addElement(new Task(taskText, false));
                taskInput.setText("Enter task here...");
                taskInput.setForeground(Color.GRAY);
                updateProgressBar();
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskListModel.remove(selectedIndex);
                updateProgressBar();
            }
        });

        clearButton.addActionListener(e -> {
            taskListModel.clear();
            updateProgressBar();
        });

        taskListModel.addListDataListener(new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent e) {
                updateProgressBar();
            }

            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent e) {
                updateProgressBar();
            }

            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent e) {
                updateProgressBar();
            }
        });

        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Task task = taskListModel.getElementAt(index);
                    task.toggleCompleted();
                    taskList.repaint();
                    updateProgressBar();
                }
            }
        });

        frame.setVisible(true);
    }

    private void updateProgressBar() {
        int completedTasks = 0;
        for (int i = 0; i < taskListModel.size(); i++) {
            if (taskListModel.getElementAt(i).isCompleted()) {
                completedTasks++;
            }
        }
        int progress = taskListModel.size() > 0 ? (int) ((completedTasks / (double) taskListModel.size()) * 100) : 0;
        progressBar.setValue(progress);
        progressLabel.setText("Progress: " + progress + "%"); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoGUI::new);
    }

    static class Task {
        private String text;
        private boolean completed;

        public Task(String text, boolean completed) {
            this.text = text;
            this.completed = completed;
        }

        public String getText() {
            return text;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void toggleCompleted() {
            completed = !completed;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    static class TaskCellRenderer extends JCheckBox implements ListCellRenderer<Task> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getText());
            setSelected(value.isCompleted());
            setEnabled(true);
            setBackground(isSelected ? new Color(200, 220, 240) : Color.LIGHT_GRAY);
            return this;
        }
    }
}
