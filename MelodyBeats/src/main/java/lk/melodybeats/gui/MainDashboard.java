/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package lk.melodybeats.gui;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lk.melodybeats.util.DBConnection;
import java.sql.Date;
import lk.melodybeats.util.PasswordUtil;
import javax.swing.ListSelectionModel;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.sql.Timestamp;

public class MainDashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainDashboard.class.getName());

    private Integer selectedPromotionId = null;
    private Integer selectedUserId = null;
    private int loggedInUserId = -1;
    private String loggedInFullName = "User";

    public MainDashboard() {

        initComponents();

        // Replace the fixed outer GroupLayout with a responsive dashboard layout.
        fitDashboardToWindow();

        // Give the Promotion form evenly aligned labels and input fields.
        alignPromotionFormFields();

        // Align the Music Item controls inside the fixed dashboard window.
        alignMusicItemForm();

        // Hide the JTabbedPane headings. Navigation is handled by the sidebar.
        hideDashboardTabHeaders();

        setTitle("Melody Beats - Dashboard");
        setMinimumSize(new java.awt.Dimension(750, 850));
        setPreferredSize(new java.awt.Dimension(750, 850));
        setSize(750, 850);
        setLocationRelativeTo(null);
        setResizable(false);

        lblUserRole.setText("Store Manager");
        dashboardTabs.setSelectedComponent(tabHome);

        loadDashboardData();
        loadGenreComboBoxes();
        loadFormatComboBox();

        configureMusicDataTables();
        configureMusicDataButtons();
        loadMusicItems(tblMusicItemss, "ALL");
        loadMusicItems(tblPurchaseItems, "ALL");

        configurePromotionTable();
        loadPromotionMusicItems();
        loadPromotions();

        configureUsersTable();
        loadUserRoles();
        loadUsers();
        clearUserFields();

        configureSalesReportTable();
        loadReportTypes();
        txtReportDate.setText(LocalDate.now().toString());
        displayReportTotals(0, 0, BigDecimal.ZERO);
    }

    public MainDashboard(
            int userId,
            String fullName,
            String role) {

        this();
        setLoggedInUser(userId, fullName, role);
    }

    public void setLoggedInUser(
            int userId,
            String fullName,
            String role) {

        loggedInUserId = userId;
        loggedInFullName = fullName == null
                ? "User"
                : fullName;

        applyUserRole(role);
    }

    /**
     * Arranges the existing header, sidebar and page area so they use the
     * available window space. This method is outside the NetBeans generated
     * block, so it does not damage MainDashboard.form.
     */
    private void fitDashboardToWindow() {
        java.awt.Container contentPane = getContentPane();

        contentPane.removeAll();
        contentPane.setLayout(new java.awt.BorderLayout());

        headerPanel.removeAll();
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.setPreferredSize(new java.awt.Dimension(750, 55));
        headerPanel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 15)
        );
        



        lblAppName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.JPanel accountPanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 0)
        );
        accountPanel.setOpaque(false);
        accountPanel.add(lblUserRole);
        accountPanel.add(btnLogout);

        // A matching spacer keeps the application title visually centred.
        javax.swing.JPanel headerSpacer = new javax.swing.JPanel();
        headerSpacer.setOpaque(false);
        headerSpacer.setPreferredSize(new java.awt.Dimension(175, 1));

        headerPanel.add(headerSpacer, java.awt.BorderLayout.WEST);
        headerPanel.add(lblAppName, java.awt.BorderLayout.CENTER);
        headerPanel.add(accountPanel, java.awt.BorderLayout.EAST);

        sidebarPanel.removeAll();
        sidebarPanel.setLayout(
                new javax.swing.BoxLayout(
                        sidebarPanel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );
        sidebarPanel.setPreferredSize(new java.awt.Dimension(135, 0));
        sidebarPanel.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(30, 10, 15, 10)
        );

        javax.swing.JButton[] navigationButtons = {
            btnHome,
            btnMusicItems,
            btnPurchase,
            btnPromotions,
            btnUsers,
            btnReports
        };

        for (javax.swing.JButton button : navigationButtons) {
            button.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new java.awt.Dimension(110, 30));
            button.setPreferredSize(new java.awt.Dimension(110, 30));
            sidebarPanel.add(button);
            sidebarPanel.add(javax.swing.Box.createVerticalStrut(12));
        }

        dashboardTabs.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        contentPane.add(headerPanel, java.awt.BorderLayout.NORTH);
        contentPane.add(sidebarPanel, java.awt.BorderLayout.WEST);
        contentPane.add(dashboardTabs, java.awt.BorderLayout.CENTER);

        contentPane.revalidate();
        contentPane.repaint();
    }

    /**
     * Rebuilds only the Promotion Details panel layout after NetBeans creates
     * the components. All input fields receive matching widths and alignment.
     */
    private void alignPromotionFormFields() {
        pnlPromotionDetails.removeAll();
        pnlPromotionDetails.setLayout(new java.awt.GridBagLayout());

        lblPramotionDetails.setText("PROMOTION DETAILS");
        jLabel6.setText("Promotion Name");
        btnAddPromotion.setText("ADD PROMOTION");
        btnClearPromotion.setText("CLEAR");

        txtPromotionName.setColumns(12);
        txtDiscountPercentage.setColumns(6);
        txtDiscountPercentage.setPreferredSize(
                new java.awt.Dimension(70, 28)
        );
        txtPromotionStartDate.setColumns(12);
        txtPromotionEndDate.setColumns(12);

        java.awt.GridBagConstraints constraints;

        constraints = promotionFormConstraints(0, 0);
        constraints.gridwidth = 4;
        constraints.insets = new java.awt.Insets(8, 8, 14, 8);
        pnlPromotionDetails.add(lblPramotionDetails, constraints);

        constraints = promotionFormConstraints(0, 1);
        pnlPromotionDetails.add(jLabel3, constraints);

        constraints = promotionFormConstraints(1, 1);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlPromotionDetails.add(cmbPromotionItem, constraints);

        constraints = promotionFormConstraints(2, 1);
        constraints.insets = new java.awt.Insets(6, 24, 6, 8);
        pnlPromotionDetails.add(jLabel6, constraints);

        constraints = promotionFormConstraints(3, 1);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlPromotionDetails.add(txtPromotionName, constraints);

        constraints = promotionFormConstraints(0, 2);
        pnlPromotionDetails.add(jLabel4, constraints);

        constraints = promotionFormConstraints(1, 2);
        pnlPromotionDetails.add(txtDiscountPercentage, constraints);

        constraints = promotionFormConstraints(2, 2);
        constraints.insets = new java.awt.Insets(6, 24, 6, 8);
        pnlPromotionDetails.add(jLabel8, constraints);

        constraints = promotionFormConstraints(3, 2);
        pnlPromotionDetails.add(chkPromotionActive, constraints);

        constraints = promotionFormConstraints(0, 3);
        pnlPromotionDetails.add(jLabel5, constraints);

        constraints = promotionFormConstraints(1, 3);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlPromotionDetails.add(txtPromotionStartDate, constraints);

        constraints = promotionFormConstraints(2, 3);
        constraints.insets = new java.awt.Insets(6, 24, 6, 8);
        pnlPromotionDetails.add(jLabel7, constraints);

        constraints = promotionFormConstraints(3, 3);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlPromotionDetails.add(txtPromotionEndDate, constraints);

        javax.swing.JPanel promotionButtonPanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 0)
        );
        promotionButtonPanel.setOpaque(false);
        promotionButtonPanel.add(btnAddPromotion);
        promotionButtonPanel.add(btnUpdatePromotion);
        promotionButtonPanel.add(btnDeactivatePromotion);
        promotionButtonPanel.add(btnClearPromotion);
        promotionButtonPanel.add(btnRefreshPromotions);

        constraints = promotionFormConstraints(0, 4);
        constraints.gridwidth = 4;
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.insets = new java.awt.Insets(16, 0, 6, 0);
        pnlPromotionDetails.add(promotionButtonPanel, constraints);

        constraints = promotionFormConstraints(0, 5);
        constraints.gridwidth = 4;
        constraints.weighty = 1.0;
        constraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlPromotionDetails.add(javax.swing.Box.createGlue(), constraints);

        pnlPromotionDetails.revalidate();
        pnlPromotionDetails.repaint();
    }

    private java.awt.GridBagConstraints promotionFormConstraints(
            int column,
            int row) {

        java.awt.GridBagConstraints constraints
                = new java.awt.GridBagConstraints();

        constraints.gridx = column;
        constraints.gridy = row;
        constraints.anchor = java.awt.GridBagConstraints.WEST;
        constraints.insets = new java.awt.Insets(6, 8, 6, 8);

        return constraints;
    }

    /**
     * Aligns the Music Items search area, table and form without changing the
     * NetBeans generated component declarations.
     */
    private void alignMusicItemForm() {
        tabMusicItems.removeAll();
        tabMusicItems.setLayout(new java.awt.BorderLayout(8, 8));
        tabMusicItems.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        jPanel2.removeAll();
        jPanel2.setLayout(
                new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5)
        );
        cmbSearchGenre.setPreferredSize(new java.awt.Dimension(145, 28));
        jPanel2.add(lblSearchGenre);
        jPanel2.add(cmbSearchGenre);
        jPanel2.add(btnSearchMusic);
        jPanel2.add(btnViewAllMusic);

        javax.swing.JPanel musicHeader = new javax.swing.JPanel(
                new java.awt.BorderLayout(0, 8)
        );
        musicHeader.setOpaque(false);
        musicHeader.add(lblMusicItemsHeading, java.awt.BorderLayout.NORTH);
        musicHeader.add(jPanel2, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel musicForm = new javax.swing.JPanel(
                new java.awt.GridBagLayout()
        );
        musicForm.setOpaque(false);

        txtTitle.setPreferredSize(new java.awt.Dimension(160, 28));
        txtArtist.setPreferredSize(new java.awt.Dimension(160, 28));
        txtPrice.setPreferredSize(new java.awt.Dimension(160, 28));
        cmbGenre.setPreferredSize(new java.awt.Dimension(160, 28));
        cmbFormat.setPreferredSize(new java.awt.Dimension(160, 28));

        java.awt.GridBagConstraints constraints;

        constraints = musicFormConstraints(0, 0);
        musicForm.add(lblTitle, constraints);

        constraints = musicFormConstraints(1, 0);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        musicForm.add(txtTitle, constraints);

        constraints = musicFormConstraints(2, 0);
        constraints.insets = new java.awt.Insets(6, 22, 6, 8);
        musicForm.add(lblArtist, constraints);

        constraints = musicFormConstraints(3, 0);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        musicForm.add(txtArtist, constraints);

        constraints = musicFormConstraints(0, 1);
        musicForm.add(lblGenre, constraints);

        constraints = musicFormConstraints(1, 1);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        musicForm.add(cmbGenre, constraints);

        constraints = musicFormConstraints(2, 1);
        constraints.insets = new java.awt.Insets(6, 22, 6, 8);
        musicForm.add(lblFormat, constraints);

        constraints = musicFormConstraints(3, 1);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        musicForm.add(cmbFormat, constraints);

        constraints = musicFormConstraints(0, 2);
        musicForm.add(lblPrice, constraints);

        constraints = musicFormConstraints(1, 2);
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        musicForm.add(txtPrice, constraints);

        javax.swing.JPanel musicButtonPanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0)
        );
        musicButtonPanel.setOpaque(false);
        musicButtonPanel.add(btnAddMusicItem);
        musicButtonPanel.add(btnUpdateMusicItem);
        musicButtonPanel.add(btnClearMusicFields);

        constraints = musicFormConstraints(0, 3);
        constraints.gridwidth = 4;
        constraints.weightx = 1.0;
        constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        constraints.insets = new java.awt.Insets(14, 0, 4, 0);
        musicForm.add(musicButtonPanel, constraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 300));

        tabMusicItems.add(musicHeader, java.awt.BorderLayout.NORTH);
        tabMusicItems.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        tabMusicItems.add(musicForm, java.awt.BorderLayout.SOUTH);

        tabMusicItems.revalidate();
        tabMusicItems.repaint();
    }

    private java.awt.GridBagConstraints musicFormConstraints(
            int column,
            int row) {

        java.awt.GridBagConstraints constraints
                = new java.awt.GridBagConstraints();

        constraints.gridx = column;
        constraints.gridy = row;
        constraints.anchor = java.awt.GridBagConstraints.WEST;
        constraints.insets = new java.awt.Insets(6, 8, 6, 8);

        return constraints;
    }

    /**
     * Hides the Home, Music Items, Purchase, Promotions, Users and Reports
     * headings at the top of the dashboard. The pages are still available and
     * are opened by the sidebar buttons.
     */
    private void hideDashboardTabHeaders() {
        dashboardTabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(
                    int tabPlacement,
                    int horizontalRunCount,
                    int maximumTabHeight) {

                return 0;
            }

            @Override
            protected void paintTabArea(
                    java.awt.Graphics graphics,
                    int tabPlacement,
                    int selectedIndex) {

                // The dashboard uses sidebar buttons instead of tab headings.
            }
        });

        dashboardTabs.setFocusable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        lblUserRole = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        sidebarPanel = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnMusicItems = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btnPromotions = new javax.swing.JButton();
        btnUsers = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        dashboardTabs = new javax.swing.JTabbedPane();
        tabHome = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        cardMusicItems = new javax.swing.JPanel();
        lblMusicTitle = new javax.swing.JLabel();
        lblMusicCount = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblSalesTitle = new javax.swing.JLabel();
        lblTodaySales = new javax.swing.JLabel();
        cardPromotions = new javax.swing.JPanel();
        lblPromotionTitle = new javax.swing.JLabel();
        lblPromotionCount = new javax.swing.JLabel();
        lblRecentSales = new javax.swing.JLabel();
        recentSalesScrollPane = new javax.swing.JScrollPane();
        tblMusicItems = new javax.swing.JTable();
        tabMusicItems = new javax.swing.JPanel();
        lblMusicItemsHeading = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblSearchGenre = new javax.swing.JLabel();
        cmbSearchGenre = new javax.swing.JComboBox<>();
        btnSearchMusic = new javax.swing.JButton();
        btnViewAllMusic = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMusicItemss = new javax.swing.JTable();
        lblTitle = new javax.swing.JLabel();
        lblGenre = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblArtist = new javax.swing.JLabel();
        lblFormat = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtArtist = new javax.swing.JTextField();
        cmbGenre = new javax.swing.JComboBox<>();
        cmbFormat = new javax.swing.JComboBox<>();
        btnAddMusicItem = new javax.swing.JButton();
        btnUpdateMusicItem = new javax.swing.JButton();
        btnClearMusicFields = new javax.swing.JButton();
        tabPromotions = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPromotions = new javax.swing.JTable();
        pnlPromotionDetails = new javax.swing.JPanel();
        lblPramotionDetails = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbPromotionItem = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtPromotionName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDiscountPercentage = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        chkPromotionActive = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtPromotionStartDate = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPromotionEndDate = new javax.swing.JTextField();
        btnAddPromotion = new javax.swing.JButton();
        btnUpdatePromotion = new javax.swing.JButton();
        btnDeactivatePromotion = new javax.swing.JButton();
        btnClearPromotion = new javax.swing.JButton();
        btnRefreshPromotions = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tabUsers = new javax.swing.JPanel();
        tabManageUsers = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblUserPassword = new javax.swing.JLabel();
        lblFullName = new javax.swing.JLabel();
        lblAccUserRole = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        chkUserActive = new javax.swing.JCheckBox();
        txtUsername = new javax.swing.JTextField();
        txtFullName = new javax.swing.JTextField();
        cmbUserRole = new javax.swing.JComboBox<>();
        btnAddUser = new javax.swing.JButton();
        btnUpdateUser = new javax.swing.JButton();
        btnDeactivateUser = new javax.swing.JButton();
        btnClearUser = new javax.swing.JButton();
        btnRefreshUsers = new javax.swing.JButton();
        txtUserPassword = new javax.swing.JPasswordField();
        tabReports = new javax.swing.JPanel();
        tabSalesReports = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblReportType = new javax.swing.JLabel();
        cmbReportType = new javax.swing.JComboBox<>();
        lblReportDate = new javax.swing.JLabel();
        txtReportDate = new javax.swing.JTextField();
        lblDateFormat = new javax.swing.JLabel();
        btnGenerateReport = new javax.swing.JButton();
        btnViewToday = new javax.swing.JButton();
        btnRefreshReport = new javax.swing.JButton();
        lblTotalSalesValue1 = new javax.swing.JLabel();
        lblItemsSoldValue1 = new javax.swing.JLabel();
        lblTotalRevenueValue1 = new javax.swing.JLabel();
        lblTotalSalesValue = new javax.swing.JLabel();
        lblItemsSoldValue = new javax.swing.JLabel();
        lblTotalRevenueValue = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblSalesReport = new javax.swing.JTable();
        lblGrandTotalValue1 = new javax.swing.JLabel();
        lblGrandTotalValue = new javax.swing.JLabel();
        tabPurchase = new javax.swing.JPanel();
        lblPurchaseTitle = new javax.swing.JLabel();
        lblPurchaseGenre = new javax.swing.JLabel();
        cmbPurchaseGenre = new javax.swing.JComboBox<>();
        btnSearchPurchaseItems = new javax.swing.JButton();
        btnViewAllPurchaseItems = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPurchaseItems = new javax.swing.JTable();
        lblQuantity = new javax.swing.JLabel();
        btnAddToCart = new javax.swing.JButton();
        lblCartTitle = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPurchaseCart = new javax.swing.JTable();
        btnRemoveCartItem = new javax.swing.JButton();
        btnClearCart = new javax.swing.JButton();
        lblSubtotalText = new javax.swing.JLabel();
        lblDiscountText = new javax.swing.JLabel();
        lblTotalText = new javax.swing.JLabel();
        lblSubtotalValue = new javax.swing.JLabel();
        lblDiscountValue = new javax.swing.JLabel();
        lblTotalValue = new javax.swing.JLabel();
        btnCompletePurchase = new javax.swing.JButton();
        spnQuantity = new javax.swing.JSpinner();
        lblAppName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Melody Beats - Dashboard");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(700, 750));
        setPreferredSize(new java.awt.Dimension(750, 1000));

        headerPanel.setMinimumSize(new java.awt.Dimension(800, 60));

        lblUserRole.setText("Store Manager");

        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(525, 525, 525)
                .addComponent(lblUserRole)
                .addGap(18, 18, 18)
                .addComponent(btnLogout)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserRole)
                    .addComponent(btnLogout))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidebarPanel.setLayout(new java.awt.GridLayout(1, 0, 7, 5));

        btnHome.setText("HOME");
        btnHome.addActionListener(this::btnHomeActionPerformed);

        btnMusicItems.setText("MUSIC ITEMS");
        btnMusicItems.addActionListener(this::btnMusicItemsActionPerformed);

        btnPurchase.setText("PURCHASE");
        btnPurchase.addActionListener(this::btnPurchaseActionPerformed);

        btnPromotions.setText("PROMOTIONS");
        btnPromotions.addActionListener(this::btnPromotionsActionPerformed);

        btnUsers.setText("USERS");
        btnUsers.addActionListener(this::btnUsersActionPerformed);

        btnReports.setText("REPORTS");
        btnReports.addActionListener(this::btnReportsActionPerformed);

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setText("Welcome to Melody Beats");
        lblWelcome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblMusicTitle.setText("MUSIC ITEMS");

        lblMusicCount.setText(" ");

        javax.swing.GroupLayout cardMusicItemsLayout = new javax.swing.GroupLayout(cardMusicItems);
        cardMusicItems.setLayout(cardMusicItemsLayout);
        cardMusicItemsLayout.setHorizontalGroup(
            cardMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardMusicItemsLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(cardMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblMusicTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMusicCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        cardMusicItemsLayout.setVerticalGroup(
            cardMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardMusicItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMusicTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMusicCount)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblSalesTitle.setText("TODAY SALE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSalesTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTodaySales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSalesTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        lblPromotionTitle.setText("PROMOTIONS");

        javax.swing.GroupLayout cardPromotionsLayout = new javax.swing.GroupLayout(cardPromotions);
        cardPromotions.setLayout(cardPromotionsLayout);
        cardPromotionsLayout.setHorizontalGroup(
            cardPromotionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPromotionsLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(cardPromotionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPromotionTitle)
                    .addComponent(lblPromotionCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54))
        );
        cardPromotionsLayout.setVerticalGroup(
            cardPromotionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPromotionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPromotionTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPromotionCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        lblRecentSales.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblRecentSales.setText("RECENT SALES");
        lblRecentSales.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tblMusicItems.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblMusicItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sale ID", "Date and Time", "Assistant", "Total"
            }
        ));
        recentSalesScrollPane.setViewportView(tblMusicItems);
        if (tblMusicItems.getColumnModel().getColumnCount() > 0) {
            tblMusicItems.getColumnModel().getColumn(0).setHeaderValue("Sale ID");
            tblMusicItems.getColumnModel().getColumn(1).setHeaderValue("Date and Time");
            tblMusicItems.getColumnModel().getColumn(2).setHeaderValue("Assistant");
            tblMusicItems.getColumnModel().getColumn(3).setHeaderValue("Total");
        }

        javax.swing.GroupLayout tabHomeLayout = new javax.swing.GroupLayout(tabHome);
        tabHome.setLayout(tabHomeLayout);
        tabHomeLayout.setHorizontalGroup(
            tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recentSalesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabHomeLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblRecentSales, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabHomeLayout.createSequentialGroup()
                        .addComponent(cardMusicItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cardPromotions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        tabHomeLayout.setVerticalGroup(
            tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardPromotions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardMusicItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRecentSales, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recentSalesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(967, Short.MAX_VALUE))
        );

        dashboardTabs.addTab("Home", tabHome);

        lblMusicItemsHeading.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMusicItemsHeading.setText("Music Items");

        lblSearchGenre.setText("Genre");

        cmbSearchGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", " " }));
        cmbSearchGenre.addActionListener(this::cmbSearchGenreActionPerformed);

        btnSearchMusic.setText("SEARCH");
        btnSearchMusic.addActionListener(this::btnSearchMusicActionPerformed);

        btnViewAllMusic.setText("VIEW ALL");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblSearchGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbSearchGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnSearchMusic)
                .addGap(18, 18, 18)
                .addComponent(btnViewAllMusic)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSearchGenre)
                    .addComponent(cmbSearchGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchMusic)
                    .addComponent(btnViewAllMusic))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblMusicItemss.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Item ID", "Title", "Artist", "Genre", "Format ", "Price"
            }
        ));
        jScrollPane1.setViewportView(tblMusicItemss);

        lblTitle.setText("Title:");

        lblGenre.setText("Genre:");

        lblPrice.setText("Price:");

        lblArtist.setText("Artist:");

        lblFormat.setText("Format:");

        txtPrice.addActionListener(this::txtPriceActionPerformed);

        txtArtist.addActionListener(this::txtArtistActionPerformed);

        cmbGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAddMusicItem.setText("ADD");

        btnUpdateMusicItem.setText("UPDATE");

        btnClearMusicFields.setText("CLEAR");

        javax.swing.GroupLayout tabMusicItemsLayout = new javax.swing.GroupLayout(tabMusicItems);
        tabMusicItems.setLayout(tabMusicItemsLayout);
        tabMusicItemsLayout.setHorizontalGroup(
            tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMusicItemsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMusicItemsLayout.createSequentialGroup()
                            .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(tabMusicItemsLayout.createSequentialGroup()
                                    .addComponent(lblTitle)
                                    .addGap(30, 30, 30)
                                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(tabMusicItemsLayout.createSequentialGroup()
                                    .addComponent(lblPrice)
                                    .addGap(23, 23, 23)
                                    .addComponent(txtPrice))
                                .addGroup(tabMusicItemsLayout.createSequentialGroup()
                                    .addComponent(lblGenre)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                            .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblFormat))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(tabMusicItemsLayout.createSequentialGroup()
                            .addComponent(btnAddMusicItem)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdateMusicItem)
                            .addGap(112, 112, 112)
                            .addComponent(btnClearMusicFields)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(123, 123, 123))
            .addGroup(tabMusicItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabMusicItemsLayout.createSequentialGroup()
                        .addComponent(lblMusicItemsHeading)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        tabMusicItemsLayout.setVerticalGroup(
            tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMusicItemsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblMusicItemsHeading)
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(lblArtist)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtArtist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGenre)
                    .addComponent(lblFormat)
                    .addComponent(cmbGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrice)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabMusicItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddMusicItem)
                    .addComponent(btnUpdateMusicItem)
                    .addComponent(btnClearMusicFields))
                .addContainerGap(803, Short.MAX_VALUE))
        );

        dashboardTabs.addTab("Music Items", tabMusicItems);

        jLabel1.setText("Manage Promotions");

        tblPromotions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Pramotion ID", "Item ID", "Music Item", "Pramotion Name", "Discount", "Satrt Date", "End Date", "Active"
            }
        ));
        tblPromotions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPromotionsMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblPromotions);

        lblPramotionDetails.setText("PRAMOTION DETAILS");

        jLabel3.setText("Music Item");

        cmbPromotionItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Pramotion Name");

        jLabel4.setText("Discount %");

        jLabel8.setText("Active");

        chkPromotionActive.setSelected(true);

        jLabel5.setText("Start Date");

        jLabel7.setText("End Date");

        btnAddPromotion.setText("ADD PRAMOTION");
        btnAddPromotion.addActionListener(this::btnAddPromotionActionPerformed);

        btnUpdatePromotion.setText("UPDATE");
        btnUpdatePromotion.addActionListener(this::btnUpdatePromotionActionPerformed);

        btnDeactivatePromotion.setText("DEACTIVATE");
        btnDeactivatePromotion.addActionListener(this::btnDeactivatePromotionActionPerformed);

        btnClearPromotion.setText("CLEAN");
        btnClearPromotion.addActionListener(this::btnClearPromotionActionPerformed);

        btnRefreshPromotions.setText("REFRESH");
        btnRefreshPromotions.addActionListener(this::btnRefreshPromotionsActionPerformed);

        jLabel12.setText("YYYY-MM-DD");

        jLabel13.setText("YYYY-MM-DD");

        javax.swing.GroupLayout pnlPromotionDetailsLayout = new javax.swing.GroupLayout(pnlPromotionDetails);
        pnlPromotionDetails.setLayout(pnlPromotionDetailsLayout);
        pnlPromotionDetailsLayout.setHorizontalGroup(
            pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                        .addComponent(lblPramotionDetails)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                        .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                .addComponent(btnAddPromotion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnUpdatePromotion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDeactivatePromotion))
                            .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(cmbPromotionItem, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                        .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPromotionStartDate)
                                            .addComponent(txtDiscountPercentage))
                                        .addGap(36, 36, 36)))))
                        .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                    .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                    .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(chkPromotionActive))
                                        .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtPromotionName, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(6, 6, 6))
                                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPromotionEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnClearPromotion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnRefreshPromotions)))
                        .addGap(244, 244, 244))))
        );
        pnlPromotionDetailsLayout.setVerticalGroup(
            pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromotionDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPramotionDetails)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(cmbPromotionItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPromotionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkPromotionActive)
                    .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel8)
                        .addComponent(txtDiscountPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(txtPromotionStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPromotionEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(pnlPromotionDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddPromotion)
                    .addComponent(btnUpdatePromotion)
                    .addComponent(btnClearPromotion)
                    .addComponent(btnRefreshPromotions)
                    .addComponent(btnDeactivatePromotion))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabPromotionsLayout = new javax.swing.GroupLayout(tabPromotions);
        tabPromotions.setLayout(tabPromotionsLayout);
        tabPromotionsLayout.setHorizontalGroup(
            tabPromotionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPromotionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 728, Short.MAX_VALUE)
            .addGroup(tabPromotionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabPromotionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabPromotionsLayout.setVerticalGroup(
            tabPromotionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPromotionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(pnlPromotionDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(908, Short.MAX_VALUE))
        );

        dashboardTabs.addTab("Promotions", tabPromotions);

        jLabel2.setText("MANAGE USERS");

        jLabel9.setText("USER ACCOUNTS");

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User ID 1", "Username", "Full Name", "Role", "Active"
            }
        ));
        tblUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsersMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblUsers);

        javax.swing.GroupLayout tabManageUsersLayout = new javax.swing.GroupLayout(tabManageUsers);
        tabManageUsers.setLayout(tabManageUsersLayout);
        tabManageUsersLayout.setHorizontalGroup(
            tabManageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabManageUsersLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(tabManageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(tabManageUsersLayout.createSequentialGroup()
                        .addGroup(tabManageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        tabManageUsersLayout.setVerticalGroup(
            tabManageUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabManageUsersLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jLabel10.setText("ADD USER");

        lblUsername.setText("Username");

        lblUserPassword.setText("Password");

        lblFullName.setText("Full Name");

        lblAccUserRole.setText("Role");

        jLabel15.setText("Active");

        chkUserActive.setSelected(true);

        cmbUserRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAddUser.setText("ADD USER");
        btnAddUser.addActionListener(this::btnAddUserActionPerformed);

        btnUpdateUser.setText("UPDATE");
        btnUpdateUser.addActionListener(this::btnUpdateUserActionPerformed);

        btnDeactivateUser.setText("DEACTIVATE");
        btnDeactivateUser.addActionListener(this::btnDeactivateUserActionPerformed);

        btnClearUser.setText("CLEAR");
        btnClearUser.addActionListener(this::btnClearUserActionPerformed);

        btnRefreshUsers.setText("REFRESH");
        btnRefreshUsers.addActionListener(this::btnRefreshUsersActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAddUser)
                        .addGap(42, 42, 42)
                        .addComponent(btnUpdateUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(btnDeactivateUser)
                        .addGap(37, 37, 37)
                        .addComponent(btnClearUser)
                        .addGap(33, 33, 33)
                        .addComponent(btnRefreshUsers))
                    .addComponent(jLabel10)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblUserPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtUserPassword))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblUsername)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(69, 69, 69)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblFullName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFullName))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(32, 32, 32)
                                .addComponent(chkUserActive)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblAccUserRole, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbUserRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUsername)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUserPassword)
                            .addComponent(txtUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddUser, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnUpdateUser, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDeactivateUser, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnClearUser, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRefreshUsers, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(48, 48, 48))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chkUserActive)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblFullName)
                                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblAccUserRole)
                                    .addComponent(cmbUserRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout tabUsersLayout = new javax.swing.GroupLayout(tabUsers);
        tabUsers.setLayout(tabUsersLayout);
        tabUsersLayout.setHorizontalGroup(
            tabUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabUsersLayout.createSequentialGroup()
                .addGroup(tabUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabUsersLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabManageUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        tabUsersLayout.setVerticalGroup(
            tabUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabUsersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabManageUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(791, Short.MAX_VALUE))
        );

        dashboardTabs.addTab("Users", tabUsers);

        jLabel11.setText("SALES REPORTS");

        lblReportType.setText("Report Type");

        cmbReportType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DAILY", "MONTHLY" }));

        lblReportDate.setText("Date");

        lblDateFormat.setText("YYY-MM-DD");

        btnGenerateReport.setText("GENERATE");
        btnGenerateReport.addActionListener(this::btnGenerateReportActionPerformed);

        btnViewToday.setText("VIEW TODAY");
        btnViewToday.addActionListener(this::btnViewTodayActionPerformed);

        btnRefreshReport.setText("REFRESH");
        btnRefreshReport.addActionListener(this::btnRefreshReportActionPerformed);

        lblTotalSalesValue1.setText("TOTAL SALES");

        lblItemsSoldValue1.setText("ITEM SOLD");

        lblTotalRevenueValue1.setText("TOTAL REVENUE");

        tblSalesReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Sale ID", "DATE & Time", "Sales Assistant", "Items", "Total Amount"
            }
        ));
        jScrollPane6.setViewportView(tblSalesReport);

        lblGrandTotalValue1.setText("GRAND TOTAL:");

        lblGrandTotalValue.setText("Rs. 00.00");

        javax.swing.GroupLayout tabSalesReportsLayout = new javax.swing.GroupLayout(tabSalesReports);
        tabSalesReports.setLayout(tabSalesReportsLayout);
        tabSalesReportsLayout.setHorizontalGroup(
            tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSalesReportsLayout.createSequentialGroup()
                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabSalesReportsLayout.createSequentialGroup()
                        .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(2, 2, 2))
                                    .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                        .addComponent(lblReportType)
                                        .addGap(26, 26, 26)
                                        .addComponent(cmbReportType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(138, 138, 138)
                                        .addComponent(lblReportDate)
                                        .addGap(42, 42, 42)
                                        .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblDateFormat)
                                            .addComponent(txtReportDate)))))
                            .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnGenerateReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTotalSalesValue1)
                                    .addComponent(lblTotalSalesValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(63, 63, 63)
                                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                        .addComponent(btnViewToday)
                                        .addGap(99, 99, 99)
                                        .addComponent(btnRefreshReport))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSalesReportsLayout.createSequentialGroup()
                                        .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                                .addComponent(lblItemsSoldValue1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(tabSalesReportsLayout.createSequentialGroup()
                                                .addComponent(lblItemsSoldValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(42, 42, 42)))
                                        .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblTotalRevenueValue1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblTotalRevenueValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(47, 47, 47)))))
                        .addGap(0, 173, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSalesReportsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblGrandTotalValue1)
                        .addGap(18, 18, 18)
                        .addComponent(lblGrandTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        tabSalesReportsLayout.setVerticalGroup(
            tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSalesReportsLayout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReportType)
                    .addComponent(cmbReportType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReportDate)
                    .addComponent(txtReportDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDateFormat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerateReport)
                    .addComponent(btnViewToday)
                    .addComponent(btnRefreshReport))
                .addGap(18, 18, 18)
                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalSalesValue1)
                    .addComponent(lblItemsSoldValue1)
                    .addComponent(lblTotalRevenueValue1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabSalesReportsLayout.createSequentialGroup()
                        .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblItemsSoldValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotalRevenueValue, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tabSalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGrandTotalValue1)
                            .addComponent(lblGrandTotalValue))
                        .addGap(0, 164, Short.MAX_VALUE))
                    .addGroup(tabSalesReportsLayout.createSequentialGroup()
                        .addComponent(lblTotalSalesValue, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );

        javax.swing.GroupLayout tabReportsLayout = new javax.swing.GroupLayout(tabReports);
        tabReports.setLayout(tabReportsLayout);
        tabReportsLayout.setHorizontalGroup(
            tabReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabSalesReports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabReportsLayout.setVerticalGroup(
            tabReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabReportsLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(tabSalesReports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(671, Short.MAX_VALUE))
        );

        dashboardTabs.addTab("Reports", tabReports);

        tabPurchase.setPreferredSize(new java.awt.Dimension(873, 500));
        tabPurchase.setRequestFocusEnabled(false);

        lblPurchaseTitle.setText("Process Purches");

        lblPurchaseGenre.setText("Genre");

        cmbPurchaseGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSearchPurchaseItems.setText("SEARCH");

        btnViewAllPurchaseItems.setText("VIEW ALL");

        tblPurchaseItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Item ID", "Title", "Artist", "Genre", "Format", "Price"
            }
        ));
        jScrollPane2.setViewportView(tblPurchaseItems);

        lblQuantity.setText("Quantity");

        btnAddToCart.setText("ADD TO CART");
        btnAddToCart.addActionListener(this::btnAddToCartActionPerformed);

        lblCartTitle.setText("CART");

        tblPurchaseCart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Item ID", "Title", "Unit Price", "Quantity", "Discount %", "Line Total"
            }
        ));
        jScrollPane3.setViewportView(tblPurchaseCart);

        btnRemoveCartItem.setText("Remove Item");

        btnClearCart.setText("Clear Cart");

        lblSubtotalText.setText("Sub Total");

        lblDiscountText.setText("Discount");

        lblTotalText.setText("Total");

        lblSubtotalValue.setText("Rs.00.00");

        lblDiscountValue.setText("Rs.00.00");

        lblTotalValue.setText("Rs.00.00");

        btnCompletePurchase.setText("Complete Purches");

        spnQuantity.setModel(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));

        javax.swing.GroupLayout tabPurchaseLayout = new javax.swing.GroupLayout(tabPurchase);
        tabPurchase.setLayout(tabPurchaseLayout);
        tabPurchaseLayout.setHorizontalGroup(
            tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPurchaseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabPurchaseLayout.createSequentialGroup()
                        .addComponent(lblPurchaseTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tabPurchaseLayout.createSequentialGroup()
                        .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPurchaseLayout.createSequentialGroup()
                                .addComponent(btnRemoveCartItem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClearCart))
                            .addGroup(tabPurchaseLayout.createSequentialGroup()
                                .addComponent(lblPurchaseGenre)
                                .addGap(28, 28, 28)
                                .addComponent(cmbPurchaseGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearchPurchaseItems)
                                .addGap(18, 18, 18)
                                .addComponent(btnViewAllPurchaseItems))
                            .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(tabPurchaseLayout.createSequentialGroup()
                                    .addComponent(lblQuantity)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(spnQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnAddToCart))
                                .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCartTitle)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(tabPurchaseLayout.createSequentialGroup()
                                            .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblDiscountText)
                                                .addComponent(lblSubtotalText)
                                                .addComponent(lblTotalText))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblSubtotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblDiscountValue, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblTotalValue, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnCompletePurchase)))))
                        .addContainerGap(65, Short.MAX_VALUE))))
        );
        tabPurchaseLayout.setVerticalGroup(
            tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPurchaseLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblPurchaseTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPurchaseGenre)
                    .addComponent(cmbPurchaseGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchPurchaseItems)
                    .addComponent(btnViewAllPurchaseItems))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQuantity)
                    .addComponent(btnAddToCart)
                    .addComponent(spnQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(lblCartTitle)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRemoveCartItem)
                    .addComponent(btnClearCart)
                    .addGroup(tabPurchaseLayout.createSequentialGroup()
                        .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSubtotalText)
                            .addComponent(lblSubtotalValue))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDiscountValue)
                            .addComponent(lblDiscountText))))
                .addGap(25, 25, 25)
                .addGroup(tabPurchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalValue)
                    .addComponent(lblTotalText))
                .addGap(18, 18, 18)
                .addComponent(btnCompletePurchase)
                .addContainerGap(726, Short.MAX_VALUE))
        );

        dashboardTabs.addTab("Purchase", tabPurchase);

        lblAppName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAppName.setText("MELODY BEATS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPromotions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMusicItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(dashboardTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAppName, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 704, Short.MAX_VALUE)))
                .addGap(1200, 1200, 1200))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAppName))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(sidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(439, 439, 439))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(dashboardTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1348, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btnHome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMusicItems)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPurchase)
                        .addGap(13, 13, 13)
                        .addComponent(btnPromotions)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUsers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReports)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        dashboardTabs.setSelectedComponent(tabHome);
        loadDashboardData();// TODO add your handling code here:
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnMusicItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMusicItemsActionPerformed
        dashboardTabs.setSelectedComponent(tabMusicItems);
        loadMusicItems(tblMusicItemss, "ALL");
    }//GEN-LAST:event_btnMusicItemsActionPerformed

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        dashboardTabs.setSelectedComponent(tabPurchase);
        loadMusicItems(tblPurchaseItems, "ALL");
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void btnPromotionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromotionsActionPerformed
        dashboardTabs.setSelectedComponent(tabPromotions);
        loadPromotionMusicItems();
        loadPromotions();
    }//GEN-LAST:event_btnPromotionsActionPerformed

    private void btnUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsersActionPerformed
        dashboardTabs.setSelectedComponent(tabUsers);
        loadUsers();
        clearUserFields();
    }//GEN-LAST:event_btnUsersActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        dashboardTabs.setSelectedComponent(tabReports);
        viewTodaySalesReport();
    }//GEN-LAST:event_btnReportsActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void txtArtistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtArtistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtArtistActionPerformed

    private void cmbSearchGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchGenreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSearchGenreActionPerformed

    private void btnSearchMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchMusicActionPerformed
        // Search is handled by configureMusicDataButtons().
    }//GEN-LAST:event_btnSearchMusicActionPerformed

    private void btnAddToCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToCartActionPerformed
        addSelectedItemToCart();
    }//GEN-LAST:event_btnAddToCartActionPerformed

    private void btnRefreshPromotionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPromotionsActionPerformed
        loadPromotionMusicItems();
        loadPromotions();
        clearPromotionFields();
    }//GEN-LAST:event_btnRefreshPromotionsActionPerformed

    private void btnUpdatePromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePromotionActionPerformed
        updatePromotion();
    }//GEN-LAST:event_btnUpdatePromotionActionPerformed

    private void btnDeactivatePromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeactivatePromotionActionPerformed
        deactivatePromotion();
    }//GEN-LAST:event_btnDeactivatePromotionActionPerformed

    private void tblPromotionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPromotionsMouseClicked
        selectPromotionFromTable();
    }//GEN-LAST:event_tblPromotionsMouseClicked


    private void btnAddPromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPromotionActionPerformed
        addPromotion();
    }//GEN-LAST:event_btnAddPromotionActionPerformed

    private void btnClearPromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPromotionActionPerformed
        clearPromotionFields();
    }//GEN-LAST:event_btnClearPromotionActionPerformed

    private void btnUpdateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateUserActionPerformed
        updateUser();// TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateUserActionPerformed

    private void btnRefreshUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshUsersActionPerformed
        loadUsers();
        clearUserFields();// TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshUsersActionPerformed

    private void tblUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsersMouseClicked
        selectUserFromTable();// TODO add your handling code here:
    }//GEN-LAST:event_tblUsersMouseClicked

    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        addUser();// TODO add your handling code here:
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void btnDeactivateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeactivateUserActionPerformed
        deactivateUser();// TODO add your handling code here:
    }//GEN-LAST:event_btnDeactivateUserActionPerformed

    private void btnClearUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearUserActionPerformed
        clearUserFields();// TODO add your handling code here:
    }//GEN-LAST:event_btnClearUserActionPerformed

    private void btnGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateReportActionPerformed
        generateSalesReport();// TODO add your handling code here:
    }//GEN-LAST:event_btnGenerateReportActionPerformed

    private void btnViewTodayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTodayActionPerformed
        viewTodaySalesReport();// TODO add your handling code here:
    }//GEN-LAST:event_btnViewTodayActionPerformed

    private void btnRefreshReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshReportActionPerformed
        refreshSalesReport(); // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshReportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMusicItem;
    private javax.swing.JButton btnAddPromotion;
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnClearCart;
    private javax.swing.JButton btnClearMusicFields;
    private javax.swing.JButton btnClearPromotion;
    private javax.swing.JButton btnClearUser;
    private javax.swing.JButton btnCompletePurchase;
    private javax.swing.JButton btnDeactivatePromotion;
    private javax.swing.JButton btnDeactivateUser;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMusicItems;
    private javax.swing.JButton btnPromotions;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JButton btnRefreshPromotions;
    private javax.swing.JButton btnRefreshReport;
    private javax.swing.JButton btnRefreshUsers;
    private javax.swing.JButton btnRemoveCartItem;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnSearchMusic;
    private javax.swing.JButton btnSearchPurchaseItems;
    private javax.swing.JButton btnUpdateMusicItem;
    private javax.swing.JButton btnUpdatePromotion;
    private javax.swing.JButton btnUpdateUser;
    private javax.swing.JButton btnUsers;
    private javax.swing.JButton btnViewAllMusic;
    private javax.swing.JButton btnViewAllPurchaseItems;
    private javax.swing.JButton btnViewToday;
    private javax.swing.JPanel cardMusicItems;
    private javax.swing.JPanel cardPromotions;
    private javax.swing.JCheckBox chkPromotionActive;
    private javax.swing.JCheckBox chkUserActive;
    private javax.swing.JComboBox<String> cmbFormat;
    private javax.swing.JComboBox<String> cmbGenre;
    private javax.swing.JComboBox<String> cmbPromotionItem;
    private javax.swing.JComboBox<String> cmbPurchaseGenre;
    private javax.swing.JComboBox<String> cmbReportType;
    private javax.swing.JComboBox<String> cmbSearchGenre;
    private javax.swing.JComboBox<String> cmbUserRole;
    private javax.swing.JTabbedPane dashboardTabs;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblAccUserRole;
    private javax.swing.JLabel lblAppName;
    private javax.swing.JLabel lblArtist;
    private javax.swing.JLabel lblCartTitle;
    private javax.swing.JLabel lblDateFormat;
    private javax.swing.JLabel lblDiscountText;
    private javax.swing.JLabel lblDiscountValue;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblGrandTotalValue;
    private javax.swing.JLabel lblGrandTotalValue1;
    private javax.swing.JLabel lblItemsSoldValue;
    private javax.swing.JLabel lblItemsSoldValue1;
    private javax.swing.JLabel lblMusicCount;
    private javax.swing.JLabel lblMusicItemsHeading;
    private javax.swing.JLabel lblMusicTitle;
    private javax.swing.JLabel lblPramotionDetails;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblPromotionCount;
    private javax.swing.JLabel lblPromotionTitle;
    private javax.swing.JLabel lblPurchaseGenre;
    private javax.swing.JLabel lblPurchaseTitle;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblRecentSales;
    private javax.swing.JLabel lblReportDate;
    private javax.swing.JLabel lblReportType;
    private javax.swing.JLabel lblSalesTitle;
    private javax.swing.JLabel lblSearchGenre;
    private javax.swing.JLabel lblSubtotalText;
    private javax.swing.JLabel lblSubtotalValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTodaySales;
    private javax.swing.JLabel lblTotalRevenueValue;
    private javax.swing.JLabel lblTotalRevenueValue1;
    private javax.swing.JLabel lblTotalSalesValue;
    private javax.swing.JLabel lblTotalSalesValue1;
    private javax.swing.JLabel lblTotalText;
    private javax.swing.JLabel lblTotalValue;
    private javax.swing.JLabel lblUserPassword;
    private javax.swing.JLabel lblUserRole;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlPromotionDetails;
    private javax.swing.JScrollPane recentSalesScrollPane;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JSpinner spnQuantity;
    private javax.swing.JPanel tabHome;
    private javax.swing.JPanel tabManageUsers;
    private javax.swing.JPanel tabMusicItems;
    private javax.swing.JPanel tabPromotions;
    private javax.swing.JPanel tabPurchase;
    private javax.swing.JPanel tabReports;
    private javax.swing.JPanel tabSalesReports;
    private javax.swing.JPanel tabUsers;
    private javax.swing.JTable tblMusicItems;
    private javax.swing.JTable tblMusicItemss;
    private javax.swing.JTable tblPromotions;
    private javax.swing.JTable tblPurchaseCart;
    private javax.swing.JTable tblPurchaseItems;
    private javax.swing.JTable tblSalesReport;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextField txtArtist;
    private javax.swing.JTextField txtDiscountPercentage;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtPromotionEndDate;
    private javax.swing.JTextField txtPromotionName;
    private javax.swing.JTextField txtPromotionStartDate;
    private javax.swing.JTextField txtReportDate;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JPasswordField txtUserPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    private void loadDashboardData() {
        String musicCountSql
                = "SELECT COUNT(*) AS ItemCount FROM MUSIC_ITEM";

        String todaySalesSql
                = "SELECT COALESCE(SUM(TotalAmount), 0) AS TodayTotal "
                + "FROM SALE "
                + "WHERE CAST(SaleDateTime AS DATE) = "
                + "CAST(GETDATE() AS DATE)";

        String promotionCountSql
                = "SELECT COUNT(*) AS PromotionCount "
                + "FROM PROMOTION "
                + "WHERE Active = 1 "
                + "AND CAST(GETDATE() AS DATE) "
                + "BETWEEN StartDate AND EndDate";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement statement
                    = connection.prepareStatement(musicCountSql); ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    lblMusicCount.setText(
                            String.valueOf(result.getInt("ItemCount"))
                    );
                }
            }

            try (PreparedStatement statement
                    = connection.prepareStatement(todaySalesSql); ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    BigDecimal total
                            = result.getBigDecimal("TodayTotal");

                    lblTodaySales.setText(
                            String.format("Rs. %,.2f", total)
                    );
                }
            }

            try (PreparedStatement statement
                    = connection.prepareStatement(promotionCountSql); ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    lblPromotionCount.setText(
                            String.valueOf(
                                    result.getInt("PromotionCount")
                            )
                    );
                }
            }

            loadRecentSales(connection);

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load dashboard data.\n"
                    + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadRecentSales(Connection connection)
            throws SQLException {

        String sql
                = "SELECT TOP 10 "
                + "s.SaleID, "
                + "s.SaleDateTime, "
                + "u.FullName, "
                + "s.TotalAmount "
                + "FROM SALE s "
                + "INNER JOIN USER_ACCOUNT u "
                + "ON s.UserID = u.UserID "
                + "ORDER BY s.SaleDateTime DESC";

        DefaultTableModel model
                = (DefaultTableModel) tblMusicItems.getModel();

        model.setRowCount(0);

        try (PreparedStatement statement
                = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                    result.getInt("SaleID"),
                    result.getTimestamp("SaleDateTime"),
                    result.getString("FullName"),
                    String.format(
                    "Rs. %,.2f",
                    result.getBigDecimal("TotalAmount")
                    )
                };

                model.addRow(row);
            }
        }
    }

    private void loadGenreComboBoxes() {

        String sql
                = "SELECT GenreName FROM GENRE "
                + "ORDER BY GenreID";

        cmbSearchGenre.removeAllItems();
        cmbGenre.removeAllItems();
        cmbPurchaseGenre.removeAllItems();

        cmbSearchGenre.addItem("ALL");
        cmbPurchaseGenre.addItem("ALL");

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql); ResultSet result
                = statement.executeQuery()) {

            while (result.next()) {

                String genreName
                        = result.getString("GenreName");

                cmbSearchGenre.addItem(genreName);
                cmbGenre.addItem(genreName);
                cmbPurchaseGenre.addItem(genreName);
            }

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load genres.\n"
                    + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadFormatComboBox() {

        cmbFormat.removeAllItems();

        cmbFormat.addItem("PHYSICAL_ALBUM");
        cmbFormat.addItem("DIGITAL_TRACK");
    }

    private void configureMusicDataTables() {

        String[] columns = {
            "Item ID",
            "Title",
            "Artist",
            "Genre",
            "Format",
            "Price"
        };

        tblMusicItemss.setModel(createReadOnlyTableModel(columns));
        tblPurchaseItems.setModel(createReadOnlyTableModel(columns));

        String[] cartColumns = {
            "Item ID",
            "Title",
            "Unit Price",
            "Quantity",
            "Discount %",
            "Line Total"
        };

        tblPurchaseCart.setModel(
                createReadOnlyTableModel(cartColumns)
        );

        tblMusicItemss.setRowHeight(25);
        tblPurchaseItems.setRowHeight(25);
        tblPurchaseCart.setRowHeight(25);

        tblMusicItemss.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblPurchaseItems.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblPurchaseCart.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        updateCartTotals();
    }

    private DefaultTableModel createReadOnlyTableModel(
            String[] columns) {

        return new DefaultTableModel(
                new Object[][]{},
                columns
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void configureMusicDataButtons() {

        btnAddMusicItem.addActionListener(event
                -> addMusicItem()
        );

        btnUpdateMusicItem.addActionListener(event
                -> updateMusicItem()
        );

        btnClearMusicFields.addActionListener(event
                -> clearMusicItemFields()
        );

        tblMusicItemss.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateMusicItemFields();
            }
        });

        btnSearchMusic.addActionListener(event
                -> loadMusicItems(
                        tblMusicItemss,
                        getSelectedGenre(cmbSearchGenre)
                )
        );

        btnViewAllMusic.addActionListener(event -> {
            cmbSearchGenre.setSelectedItem("ALL");
            loadMusicItems(tblMusicItemss, "ALL");
        });

        btnSearchPurchaseItems.addActionListener(event
                -> loadMusicItems(
                        tblPurchaseItems,
                        getSelectedGenre(cmbPurchaseGenre)
                )
        );

        btnViewAllPurchaseItems.addActionListener(event -> {
            cmbPurchaseGenre.setSelectedItem("ALL");
            loadMusicItems(tblPurchaseItems, "ALL");
        });

        btnRemoveCartItem.addActionListener(event
                -> removeSelectedCartItem()
        );

        btnClearCart.addActionListener(event
                -> clearPurchaseCart()
        );

        btnCompletePurchase.setText("Complete Purchase");
        btnCompletePurchase.addActionListener(event
                -> completePurchase()
        );
    }

    private boolean validateMusicItemFields() {

        if (txtTitle.getText().trim().isEmpty()) {
            showMusicValidationMessage("Please enter the music title.");
            txtTitle.requestFocus();
            return false;
        }

        if (txtArtist.getText().trim().isEmpty()) {
            showMusicValidationMessage("Please enter the artist name.");
            txtArtist.requestFocus();
            return false;
        }

        if (cmbGenre.getSelectedItem() == null) {
            showMusicValidationMessage("Please select a genre.");
            cmbGenre.requestFocus();
            return false;
        }

        if (cmbFormat.getSelectedItem() == null) {
            showMusicValidationMessage("Please select an item format.");
            cmbFormat.requestFocus();
            return false;
        }

        try {
            BigDecimal price = new BigDecimal(
                    txtPrice.getText().trim().replace(",", "")
            );

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                showMusicValidationMessage(
                        "Price must be greater than zero."
                );
                txtPrice.requestFocus();
                return false;
            }

        } catch (NumberFormatException exception) {
            showMusicValidationMessage("Please enter a valid price.");
            txtPrice.requestFocus();
            return false;
        }

        return true;
    }

    private void showMusicValidationMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Music Item Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void addMusicItem() {

        if (!validateMusicItemFields()) {
            return;
        }

        String sql
                = "INSERT INTO MUSIC_ITEM "
                + "(Title, Artist, GenreID, ItemFormat, Price) "
                + "SELECT ?, ?, GenreID, ?, ? "
                + "FROM GENRE WHERE GenreName = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, txtTitle.getText().trim());
            statement.setString(2, txtArtist.getText().trim());
            statement.setString(3, cmbFormat.getSelectedItem().toString());
            statement.setBigDecimal(
                    4,
                    new BigDecimal(
                            txtPrice.getText().trim().replace(",", "")
                    )
            );
            statement.setString(5, cmbGenre.getSelectedItem().toString());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Music item added successfully."
                );

                refreshMusicItemData();
                clearMusicItemFields();
            } else {
                showMusicValidationMessage(
                        "The selected genre could not be found."
                );
            }

        } catch (SQLException exception) {
            showMusicDatabaseError(
                    "Could not add the music item.",
                    exception
            );
        }
    }

    private void updateMusicItem() {

        int selectedViewRow = tblMusicItemss.getSelectedRow();

        if (selectedViewRow < 0) {
            showMusicValidationMessage(
                    "Select a music item from the table first."
            );
            return;
        }

        if (!validateMusicItemFields()) {
            return;
        }

        int selectedModelRow
                = tblMusicItemss.convertRowIndexToModel(selectedViewRow);

        int itemId = Integer.parseInt(
                tblMusicItemss.getModel()
                        .getValueAt(selectedModelRow, 0)
                        .toString()
        );

        String sql
                = "UPDATE MUSIC_ITEM SET "
                + "Title = ?, Artist = ?, "
                + "GenreID = (SELECT GenreID FROM GENRE "
                + "WHERE GenreName = ?), "
                + "ItemFormat = ?, Price = ? "
                + "WHERE ItemID = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, txtTitle.getText().trim());
            statement.setString(2, txtArtist.getText().trim());
            statement.setString(3, cmbGenre.getSelectedItem().toString());
            statement.setString(4, cmbFormat.getSelectedItem().toString());
            statement.setBigDecimal(
                    5,
                    new BigDecimal(
                            txtPrice.getText().trim().replace(",", "")
                    )
            );
            statement.setInt(6, itemId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Music item updated successfully."
                );

                refreshMusicItemData();
                clearMusicItemFields();
            }

        } catch (SQLException exception) {
            showMusicDatabaseError(
                    "Could not update the music item.",
                    exception
            );
        }
    }

    private void populateMusicItemFields() {

        int selectedViewRow = tblMusicItemss.getSelectedRow();

        if (selectedViewRow < 0) {
            return;
        }

        int selectedModelRow
                = tblMusicItemss.convertRowIndexToModel(selectedViewRow);

        javax.swing.table.TableModel model = tblMusicItemss.getModel();

        txtTitle.setText(model.getValueAt(selectedModelRow, 1).toString());
        txtArtist.setText(model.getValueAt(selectedModelRow, 2).toString());
        cmbGenre.setSelectedItem(
                model.getValueAt(selectedModelRow, 3).toString()
        );
        cmbFormat.setSelectedItem(
                model.getValueAt(selectedModelRow, 4).toString()
        );

        String priceText = model.getValueAt(selectedModelRow, 5)
                .toString()
                .replace("Rs.", "")
                .replace(",", "")
                .trim();

        txtPrice.setText(priceText);
    }

    private void clearMusicItemFields() {
        txtTitle.setText("");
        txtArtist.setText("");
        txtPrice.setText("");
        cmbGenre.setSelectedIndex(-1);
        cmbFormat.setSelectedIndex(-1);
        tblMusicItemss.clearSelection();
        txtTitle.requestFocus();
    }

    private void refreshMusicItemData() {
        cmbSearchGenre.setSelectedItem("ALL");
        cmbPurchaseGenre.setSelectedItem("ALL");
        loadMusicItems(tblMusicItemss, "ALL");
        loadMusicItems(tblPurchaseItems, "ALL");
        loadPromotionMusicItems();
        loadDashboardData();
    }

    private void showMusicDatabaseError(
            String message,
            SQLException exception) {

        JOptionPane.showMessageDialog(
                this,
                message + "\n\n" + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );

        exception.printStackTrace();
    }

    private String getSelectedGenre(
            javax.swing.JComboBox<String> comboBox) {

        Object selectedItem = comboBox.getSelectedItem();

        if (selectedItem == null) {
            return "ALL";
        }

        return selectedItem.toString();
    }

    private void loadMusicItems(
            javax.swing.JTable table,
            String genreName) {

        boolean showAll = genreName == null
                || "ALL".equalsIgnoreCase(genreName);

        String sql
                = "SELECT m.ItemID, m.Title, m.Artist, "
                + "g.GenreName, m.ItemFormat, m.Price "
                + "FROM MUSIC_ITEM m "
                + "INNER JOIN GENRE g "
                + "ON m.GenreID = g.GenreID ";

        if (!showAll) {
            sql += "WHERE g.GenreName = ? ";
        }

        sql += "ORDER BY m.ItemID";

        DefaultTableModel model
                = (DefaultTableModel) table.getModel();

        model.setRowCount(0);

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            if (!showAll) {
                statement.setString(1, genreName);
            }

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Object[] row = {
                        result.getInt("ItemID"),
                        result.getString("Title"),
                        result.getString("Artist"),
                        result.getString("GenreName"),
                        result.getString("ItemFormat"),
                        String.format(
                        "Rs. %,.2f",
                        result.getBigDecimal("Price")
                        )
                    };

                    model.addRow(row);
                }
            }

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load music items.\n"
                    + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();
        }
    }

    private void addSelectedItemToCart() {

        int selectedViewRow = tblPurchaseItems.getSelectedRow();

        if (selectedViewRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a music item first.",
                    "No Item Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedRow
                = tblPurchaseItems.convertRowIndexToModel(
                        selectedViewRow
                );

        int itemId = Integer.parseInt(
                tblPurchaseItems.getModel()
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        int quantity = ((Number) spnQuantity.getValue()).intValue();

        String sql
                = "SELECT m.Title, m.Price, "
                + "COALESCE(MAX(p.DiscountPercentage), 0) "
                + "AS DiscountPercentage "
                + "FROM MUSIC_ITEM m "
                + "LEFT JOIN PROMOTION p "
                + "ON m.ItemID = p.ItemID "
                + "AND p.Active = 1 "
                + "AND CAST(GETDATE() AS DATE) "
                + "BETWEEN p.StartDate AND p.EndDate "
                + "WHERE m.ItemID = ? "
                + "GROUP BY m.Title, m.Price";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, itemId);

            try (ResultSet result = statement.executeQuery()) {

                if (!result.next()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "The selected music item no longer exists.",
                            "Item Not Found",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                String title = result.getString("Title");
                BigDecimal unitPrice = result.getBigDecimal("Price");
                BigDecimal discount = result.getBigDecimal(
                        "DiscountPercentage"
                );

                addOrUpdateCartRow(
                        itemId,
                        title,
                        unitPrice,
                        quantity,
                        discount
                );
            }

            spnQuantity.setValue(1);
            updateCartTotals();

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not add the item to the cart.\n"
                    + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();
        }
    }

    private void addOrUpdateCartRow(
            int itemId,
            String title,
            BigDecimal unitPrice,
            int addedQuantity,
            BigDecimal discount) {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        int cartRow = findCartRow(itemId);
        int finalQuantity = addedQuantity;

        if (cartRow >= 0) {
            finalQuantity += Integer.parseInt(
                    cartModel.getValueAt(cartRow, 3).toString()
            );
        }

        BigDecimal lineSubtotal = unitPrice.multiply(
                BigDecimal.valueOf(finalQuantity)
        );

        BigDecimal discountAmount = lineSubtotal
                .multiply(discount)
                .divide(
                        new BigDecimal("100"),
                        2,
                        java.math.RoundingMode.HALF_UP
                );

        BigDecimal lineTotal = lineSubtotal
                .subtract(discountAmount)
                .setScale(2, java.math.RoundingMode.HALF_UP);

        if (cartRow >= 0) {
            cartModel.setValueAt(finalQuantity, cartRow, 3);
            cartModel.setValueAt(discount, cartRow, 4);
            cartModel.setValueAt(lineTotal, cartRow, 5);
        } else {
            cartModel.addRow(new Object[]{
                itemId,
                title,
                unitPrice,
                finalQuantity,
                discount,
                lineTotal
            });
        }
    }

    private int findCartRow(int itemId) {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        for (int row = 0; row < cartModel.getRowCount(); row++) {

            int existingItemId = Integer.parseInt(
                    cartModel.getValueAt(row, 0).toString()
            );

            if (existingItemId == itemId) {
                return row;
            }
        }

        return -1;
    }

    private void updateCartTotals() {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (int row = 0; row < cartModel.getRowCount(); row++) {

            BigDecimal unitPrice = new BigDecimal(
                    cartModel.getValueAt(row, 2).toString()
            );

            int quantity = Integer.parseInt(
                    cartModel.getValueAt(row, 3).toString()
            );

            BigDecimal lineTotal = new BigDecimal(
                    cartModel.getValueAt(row, 5).toString()
            );

            subtotal = subtotal.add(
                    unitPrice.multiply(
                            BigDecimal.valueOf(quantity)
                    )
            );

            total = total.add(lineTotal);
        }

        BigDecimal discountAmount = subtotal.subtract(total);

        lblSubtotalValue.setText(formatMoney(subtotal));
        lblDiscountValue.setText(formatMoney(discountAmount));
        lblTotalValue.setText(formatMoney(total));
    }

    private void removeSelectedCartItem() {

        int selectedViewRow = tblPurchaseCart.getSelectedRow();

        if (selectedViewRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an item from the cart first.",
                    "No Cart Item Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedModelRow
                = tblPurchaseCart.convertRowIndexToModel(
                        selectedViewRow
                );

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        cartModel.removeRow(selectedModelRow);
        tblPurchaseCart.clearSelection();
        updateCartTotals();
    }

    private void clearPurchaseCart() {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        cartModel.setRowCount(0);
        tblPurchaseCart.clearSelection();
        spnQuantity.setValue(1);
        updateCartTotals();
    }

    private void completePurchase() {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "The cart is empty. Add an item first.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (loggedInUserId <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please log in before completing a purchase.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        BigDecimal subtotalAmount
                = calculateCartSubtotal();

        BigDecimal totalAmount
                = calculateCartTotal();

        BigDecimal discountAmount
                = subtotalAmount.subtract(totalAmount);

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Complete this purchase for "
                + formatMoney(totalAmount) + "?",
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String saleSql
                = "INSERT INTO SALE "
                + "(SaleDateTime, UserID, Subtotal, "
                + "DiscountAmount, TotalAmount) "
                + "VALUES (?, ?, ?, ?, ?)";

        String saleItemSql
                = "INSERT INTO SALE_ITEM "
                + "(SaleID, ItemID, Quantity, UnitPrice, "
                + "DiscountPercentage, LineTotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Timestamp saleDateTime
                = new Timestamp(System.currentTimeMillis());

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int saleId;

                try (PreparedStatement saleStatement
                        = connection.prepareStatement(
                                saleSql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )) {

                            saleStatement.setTimestamp(1, saleDateTime);
                            saleStatement.setInt(2, loggedInUserId);
                            saleStatement.setBigDecimal(3, subtotalAmount);
                            saleStatement.setBigDecimal(4, discountAmount);
                            saleStatement.setBigDecimal(5, totalAmount);
                            saleStatement.executeUpdate();

                            try (ResultSet keys
                                    = saleStatement.getGeneratedKeys()) {

                                if (!keys.next()) {
                                    throw new SQLException(
                                            "The Sale ID was not generated."
                                    );
                                }

                                saleId = keys.getInt(1);
                            }
                        }

                        try (PreparedStatement itemStatement
                                = connection.prepareStatement(saleItemSql)) {

                            for (int row = 0;
                                    row < cartModel.getRowCount();
                                    row++) {

                                itemStatement.setInt(
                                        1,
                                        saleId
                                );

                                itemStatement.setInt(
                                        2,
                                        Integer.parseInt(
                                                cartModel.getValueAt(row, 0).toString()
                                        )
                                );

                                itemStatement.setInt(
                                        3,
                                        Integer.parseInt(
                                                cartModel.getValueAt(row, 3).toString()
                                        )
                                );

                                itemStatement.setBigDecimal(
                                        4,
                                        new BigDecimal(
                                                cartModel.getValueAt(row, 2).toString()
                                        )
                                );

                                itemStatement.setBigDecimal(
                                        5,
                                        new BigDecimal(
                                                cartModel.getValueAt(row, 4).toString()
                                        )
                                );

                                itemStatement.setBigDecimal(
                                        6,
                                        new BigDecimal(
                                                cartModel.getValueAt(row, 5).toString()
                                        )
                                );

                                itemStatement.addBatch();
                            }

                            itemStatement.executeBatch();
                        }

                        connection.commit();

                        String receipt = createReceiptText(
                                saleId,
                                saleDateTime,
                                cartModel
                        );

                        clearPurchaseCart();
                        loadDashboardData();

                        printReceipt(receipt, saleId);

            } catch (Exception exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not complete the purchase.\n\n"
                    + exception.getMessage(),
                    "Purchase Error",
                    JOptionPane.ERROR_MESSAGE
            );
            exception.printStackTrace();
        }
    }

    private BigDecimal calculateCartSubtotal() {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (int row = 0;
                row < cartModel.getRowCount();
                row++) {

            BigDecimal unitPrice = new BigDecimal(
                    cartModel.getValueAt(row, 2).toString()
            );

            int quantity = Integer.parseInt(
                    cartModel.getValueAt(row, 3).toString()
            );

            subtotal = subtotal.add(
                    unitPrice.multiply(
                            BigDecimal.valueOf(quantity)
                    )
            );
        }

        return subtotal.setScale(
                2,
                java.math.RoundingMode.HALF_UP
        );
    }

    private BigDecimal calculateCartTotal() {

        DefaultTableModel cartModel
                = (DefaultTableModel) tblPurchaseCart.getModel();

        BigDecimal total = BigDecimal.ZERO;

        for (int row = 0;
                row < cartModel.getRowCount();
                row++) {

            total = total.add(
                    new BigDecimal(
                            cartModel.getValueAt(row, 5).toString()
                    )
            );
        }

        return total.setScale(
                2,
                java.math.RoundingMode.HALF_UP
        );
    }

    private String createReceiptText(
            int saleId,
            Timestamp saleDateTime,
            DefaultTableModel cartModel) {

        StringBuilder receipt = new StringBuilder();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        receipt.append("          MELODY BEATS\n");
        receipt.append("        SALES RECEIPT\n");
        receipt.append("========================================\n");
        receipt.append("Receipt No : ").append(saleId).append('\n');
        receipt.append("Date       : ")
                .append(new java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"
                ).format(saleDateTime))
                .append('\n');
        receipt.append("Cashier    : ")
                .append(loggedInFullName)
                .append('\n');
        receipt.append("========================================\n");
        receipt.append(String.format(
                "%-19s %3s %15s%n",
                "Item",
                "Qty",
                "Amount"
        ));
        receipt.append("----------------------------------------\n");

        for (int row = 0;
                row < cartModel.getRowCount();
                row++) {

            String title = cartModel.getValueAt(row, 1)
                    .toString();

            if (title.length() > 19) {
                title = title.substring(0, 19);
            }

            int quantity = Integer.parseInt(
                    cartModel.getValueAt(row, 3).toString()
            );

            BigDecimal unitPrice = new BigDecimal(
                    cartModel.getValueAt(row, 2).toString()
            );

            BigDecimal lineTotal = new BigDecimal(
                    cartModel.getValueAt(row, 5).toString()
            );

            subtotal = subtotal.add(
                    unitPrice.multiply(
                            BigDecimal.valueOf(quantity)
                    )
            );
            total = total.add(lineTotal);

            receipt.append(String.format(
                    "%-19s %3d %15.2f%n",
                    title,
                    quantity,
                    lineTotal
            ));
        }

        BigDecimal discount = subtotal.subtract(total);

        receipt.append("----------------------------------------\n");
        receipt.append(String.format(
                "%-23s %15.2f%n",
                "Subtotal:",
                subtotal
        ));
        receipt.append(String.format(
                "%-23s %15.2f%n",
                "Discount:",
                discount
        ));
        receipt.append(String.format(
                "%-23s %15.2f%n",
                "TOTAL (Rs.):",
                total
        ));
        receipt.append("========================================\n");
        receipt.append("       Thank you for shopping!\n");

        return receipt.toString();
    }

    private void printReceipt(
            String receipt,
            int saleId) {

        javax.swing.JTextArea receiptArea
                = new javax.swing.JTextArea(receipt);

        receiptArea.setFont(
                new java.awt.Font(
                        java.awt.Font.MONOSPACED,
                        java.awt.Font.PLAIN,
                        11
                )
        );

        try {
            boolean printed = receiptArea.print(
                    new java.text.MessageFormat(
                            "Melody Beats - Receipt " + saleId
                    ),
                    new java.text.MessageFormat("Page {0}"),
                    true,
                    null,
                    null,
                    true
            );

            String message = printed
                    ? "Purchase completed and receipt printed successfully."
                    : "Purchase completed successfully. Printing was cancelled.";

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Purchase Completed",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (java.awt.print.PrinterException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Purchase was saved, but the receipt could not be printed.\n\n"
                    + exception.getMessage(),
                    "Printing Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private String formatMoney(BigDecimal amount) {
        return String.format("Rs. %,.2f", amount);
    }

    private void configurePromotionTable() {

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Promotion ID",
                    "Item ID",
                    "Music Item",
                    "Promotion Name",
                    "Discount %",
                    "Start Date",
                    "End Date",
                    "Active"
                }
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tblPromotions.setModel(model);
        tblPromotions.setRowHeight(25);
    }

    private void loadPromotionMusicItems() {

        String sql
                = "SELECT ItemID, Title "
                + "FROM MUSIC_ITEM "
                + "ORDER BY Title";

        cmbPromotionItem.removeAllItems();

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql); ResultSet result
                = statement.executeQuery()) {

            while (result.next()) {

                int itemId
                        = result.getInt("ItemID");

                String title
                        = result.getString("Title");

                cmbPromotionItem.addItem(
                        itemId + " - " + title
                );
            }

            cmbPromotionItem.setSelectedIndex(-1);

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load music items.\n"
                    + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadPromotions() {

        String sql
                = "SELECT p.PromotionID, "
                + "p.ItemID, "
                + "m.Title, "
                + "p.PromotionName, "
                + "p.DiscountPercentage, "
                + "p.StartDate, "
                + "p.EndDate, "
                + "p.Active "
                + "FROM PROMOTION p "
                + "INNER JOIN MUSIC_ITEM m "
                + "ON p.ItemID = m.ItemID "
                + "ORDER BY p.PromotionID DESC";

        DefaultTableModel model
                = (DefaultTableModel) tblPromotions.getModel();

        model.setRowCount(0);

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql); ResultSet result
                = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                    result.getInt("PromotionID"),
                    result.getInt("ItemID"),
                    result.getString("Title"),
                    result.getString("PromotionName"),
                    result.getBigDecimal(
                    "DiscountPercentage"
                    ),
                    result.getDate("StartDate"),
                    result.getDate("EndDate"),
                    result.getBoolean("Active")
                    ? "Yes" : "No"
                };

                model.addRow(row);
            }

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load promotions.\n"
                    + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearPromotionFields() {

        selectedPromotionId = null;

        tblPromotions.clearSelection();

        if (cmbPromotionItem.getItemCount() > 0) {
            cmbPromotionItem.setSelectedIndex(-1);
        }

        txtPromotionName.setText("");
        txtDiscountPercentage.setText("");
        txtPromotionStartDate.setText("");
        txtPromotionEndDate.setText("");

        chkPromotionActive.setSelected(true);
    }

    private int getSelectedPromotionItemId() {

        Object selectedValue
                = cmbPromotionItem.getSelectedItem();

        if (selectedValue == null) {
            throw new IllegalArgumentException(
                    "Please select a music item."
            );
        }

        String itemText = selectedValue.toString();
        int separatorPosition = itemText.indexOf(" - ");

        if (separatorPosition < 1) {
            throw new IllegalArgumentException(
                    "Invalid music-item selection."
            );
        }

        return Integer.parseInt(
                itemText.substring(
                        0,
                        separatorPosition
                )
        );
    }

    private void selectPromotionMusicItem(int itemId) {
        for (int index = 0;
                index < cmbPromotionItem.getItemCount();
                index++) {

            String item = cmbPromotionItem.getItemAt(index);

            if (item.startsWith(itemId + " - ")) {
                cmbPromotionItem.setSelectedIndex(index);
                return;
            }
        }

        cmbPromotionItem.setSelectedIndex(-1);
    }

    private void deactivatePromotion() {
        if (selectedPromotionId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a promotion from the table first.",
                    "No Promotion Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to deactivate this promotion?",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE PROMOTION SET Active = 0 "
                + "WHERE PromotionID = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, selectedPromotionId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Promotion deactivated successfully."
                );

                loadPromotions();
                loadDashboardData();
                clearPromotionFields();
            }

        } catch (SQLException exception) {
            showPromotionError(
                    "Could not deactivate the promotion.",
                    exception
            );
        }
    }

    private void selectPromotionFromTable() {

        int selectedRow
                = tblPromotions.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedPromotionId = Integer.valueOf(
                tblPromotions.getValueAt(
                        selectedRow,
                        0
                ).toString()
        );

        int itemId = Integer.parseInt(
                tblPromotions.getValueAt(
                        selectedRow,
                        1
                ).toString()
        );

        selectPromotionMusicItem(itemId);

        txtPromotionName.setText(
                tblPromotions.getValueAt(
                        selectedRow,
                        3
                ).toString()
        );

        txtDiscountPercentage.setText(
                tblPromotions.getValueAt(
                        selectedRow,
                        4
                ).toString()
        );

        txtPromotionStartDate.setText(
                tblPromotions.getValueAt(
                        selectedRow,
                        5
                ).toString()
        );

        txtPromotionEndDate.setText(
                tblPromotions.getValueAt(
                        selectedRow,
                        6
                ).toString()
        );

        String activeValue
                = tblPromotions.getValueAt(
                        selectedRow,
                        7
                ).toString();

        chkPromotionActive.setSelected(
                "Yes".equalsIgnoreCase(activeValue)
        );
    }

    private void updatePromotion() {

        if (selectedPromotionId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a promotion from the table first.",
                    "No Promotion Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validatePromotionFields()) {
            return;
        }

        String sql
                = "UPDATE PROMOTION SET "
                + "ItemID = ?, "
                + "PromotionName = ?, "
                + "DiscountPercentage = ?, "
                + "StartDate = ?, "
                + "EndDate = ?, "
                + "Active = ? "
                + "WHERE PromotionID = ?";

        try {
            int itemId
                    = getSelectedPromotionItemId();

            BigDecimal discount
                    = new BigDecimal(
                            txtDiscountPercentage
                                    .getText()
                                    .trim()
                    );

            Date startDate = Date.valueOf(
                    txtPromotionStartDate
                            .getText()
                            .trim()
            );

            Date endDate = Date.valueOf(
                    txtPromotionEndDate
                            .getText()
                            .trim()
            );

            try (Connection connection
                    = DBConnection.getConnection(); PreparedStatement statement
                    = connection.prepareStatement(sql)) {

                statement.setInt(1, itemId);

                statement.setString(
                        2,
                        txtPromotionName
                                .getText()
                                .trim()
                );

                statement.setBigDecimal(3, discount);
                statement.setDate(4, startDate);
                statement.setDate(5, endDate);

                statement.setBoolean(
                        6,
                        chkPromotionActive.isSelected()
                );

                statement.setInt(
                        7,
                        selectedPromotionId
                );

                int affectedRows
                        = statement.executeUpdate();

                if (affectedRows > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Promotion updated successfully."
                    );

                    loadPromotions();
                    clearPromotionFields();
                }
            }

        } catch (SQLException exception) {
            showPromotionError(
                    "Could not update the promotion.",
                    exception
            );

        } catch (IllegalArgumentException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private boolean validatePromotionFields() {

        if (cmbPromotionItem.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a music item.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        if (txtPromotionName.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the promotion name.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            txtPromotionName.requestFocus();
            return false;
        }

        try {
            BigDecimal discount = new BigDecimal(
                    txtDiscountPercentage
                            .getText()
                            .trim()
            );

            if (discount.compareTo(BigDecimal.ZERO) < 0
                    || discount.compareTo(
                            new BigDecimal("100")
                    ) > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Discount must be between 0 and 100.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

        } catch (NumberFormatException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid discount percentage.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            txtDiscountPercentage.requestFocus();
            return false;
        }

        try {
            Date startDate = Date.valueOf(
                    txtPromotionStartDate
                            .getText()
                            .trim()
            );

            Date endDate = Date.valueOf(
                    txtPromotionEndDate
                            .getText()
                            .trim()
            );

            if (endDate.before(startDate)) {

                JOptionPane.showMessageDialog(
                        this,
                        "End date cannot be before start date.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

        } catch (IllegalArgumentException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter dates using YYYY-MM-DD.\n"
                    + "Example: 2026-09-30",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    private void addPromotion() {

        if (!validatePromotionFields()) {
            return;
        }

        String sql
                = "INSERT INTO PROMOTION "
                + "(ItemID, PromotionName, "
                + "DiscountPercentage, StartDate, "
                + "EndDate, Active) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            int itemId
                    = getSelectedPromotionItemId();

            String promotionName
                    = txtPromotionName
                            .getText()
                            .trim();

            BigDecimal discount
                    = new BigDecimal(
                            txtDiscountPercentage
                                    .getText()
                                    .trim()
                    );

            Date startDate = Date.valueOf(
                    txtPromotionStartDate
                            .getText()
                            .trim()
            );

            Date endDate = Date.valueOf(
                    txtPromotionEndDate
                            .getText()
                            .trim()
            );

            try (Connection connection
                    = DBConnection.getConnection(); PreparedStatement statement
                    = connection.prepareStatement(sql)) {

                statement.setInt(1, itemId);
                statement.setString(2, promotionName);
                statement.setBigDecimal(3, discount);
                statement.setDate(4, startDate);
                statement.setDate(5, endDate);
                statement.setBoolean(
                        6,
                        chkPromotionActive.isSelected()
                );

                int affectedRows
                        = statement.executeUpdate();

                if (affectedRows > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Promotion added successfully."
                    );

                    loadPromotions();
                    clearPromotionFields();
                }
            }

        } catch (SQLException exception) {
            showPromotionError(
                    "Could not add the promotion.",
                    exception
            );

        } catch (IllegalArgumentException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showPromotionError(
            String message,
            SQLException exception) {

        JOptionPane.showMessageDialog(
                this,
                message + "\n\n"
                + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );

        exception.printStackTrace();
    }

    private void configureUsersTable() {

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "User ID",
                    "Username",
                    "Full Name",
                    "Role",
                    "Active"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers.setModel(model);
        tblUsers.setRowHeight(25);

        tblUsers.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void loadUserRoles() {

        cmbUserRole.removeAllItems();

        cmbUserRole.addItem("STORE_MANAGER");
        cmbUserRole.addItem("SALES_ASSISTANT");

        cmbUserRole.setSelectedIndex(-1);
    }

    private void loadUsers() {

        String sql
                = "SELECT UserID, Username, FullName, "
                + "Role, Active "
                + "FROM USER_ACCOUNT "
                + "ORDER BY UserID DESC";

        DefaultTableModel model
                = (DefaultTableModel) tblUsers.getModel();

        model.setRowCount(0);

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql); ResultSet result
                = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                    result.getInt("UserID"),
                    result.getString("Username"),
                    result.getString("FullName"),
                    result.getString("Role"),
                    result.getBoolean("Active")
                    ? "Yes" : "No"
                };

                model.addRow(row);
            }

        } catch (SQLException exception) {
            showUserDatabaseError(
                    "Could not load user accounts.",
                    exception
            );
        }
    }

    private boolean validateUserFields(
            boolean passwordRequired) {

        String username
                = txtUsername.getText().trim();

        String fullName
                = txtFullName.getText().trim();

        String password
                = new String(
                        txtUserPassword.getPassword()
                ).trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a username.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            txtUsername.requestFocus();
            return false;
        }

        if (fullName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the user's full name.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            txtFullName.requestFocus();
            return false;
        }

        if (passwordRequired && password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a password.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            txtUserPassword.requestFocus();
            return false;
        }

        if (cmbUserRole.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user role.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            cmbUserRole.requestFocus();
            return false;
        }

        return true;
    }

    private void addUser() {

        if (!validateUserFields(true)) {
            return;
        }

        String sql
                = "INSERT INTO USER_ACCOUNT "
                + "(Username, PasswordHash, "
                + "FullName, Role, Active) "
                + "VALUES (?, ?, ?, ?, ?)";

        String username
                = txtUsername.getText().trim();

        String fullName
                = txtFullName.getText().trim();

        String password
                = new String(
                        txtUserPassword.getPassword()
                );

        String role
                = cmbUserRole.getSelectedItem().toString();

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            statement.setString(
                    2,
                    PasswordUtil.hash(password)
            );

            statement.setString(3, fullName);
            statement.setString(4, role);

            statement.setBoolean(
                    5,
                    chkUserActive.isSelected()
            );

            int affectedRows
                    = statement.executeUpdate();

            if (affectedRows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "User account added successfully."
                );

                loadUsers();
                clearUserFields();
            }

        } catch (SQLException exception) {

            if (exception.getMessage()
                    .toLowerCase()
                    .contains("unique")) {

                JOptionPane.showMessageDialog(
                        this,
                        "This username is already being used.",
                        "Duplicate Username",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {
                showUserDatabaseError(
                        "Could not add the user account.",
                        exception
                );
            }
        }
    }

    private void selectUserFromTable() {

        int selectedRow
                = tblUsers.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedUserId = Integer.valueOf(
                tblUsers.getValueAt(
                        selectedRow,
                        0
                ).toString()
        );

        txtUsername.setText(
                tblUsers.getValueAt(
                        selectedRow,
                        1
                ).toString()
        );

        txtFullName.setText(
                tblUsers.getValueAt(
                        selectedRow,
                        2
                ).toString()
        );

        cmbUserRole.setSelectedItem(
                tblUsers.getValueAt(
                        selectedRow,
                        3
                ).toString()
        );

        String activeValue
                = tblUsers.getValueAt(
                        selectedRow,
                        4
                ).toString();

        chkUserActive.setSelected(
                "Yes".equalsIgnoreCase(activeValue)
        );

        // Never display the stored password hash
        txtUserPassword.setText("");
    }

    private void updateUser() {

        if (selectedUserId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a user from the table first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validateUserFields(false)) {
            return;
        }

        String username
                = txtUsername.getText().trim();

        String fullName
                = txtFullName.getText().trim();

        String password
                = new String(
                        txtUserPassword.getPassword()
                );

        String role
                = cmbUserRole.getSelectedItem().toString();

        boolean changePassword
                = !password.trim().isEmpty();

        String sql;

        if (changePassword) {
            sql = "UPDATE USER_ACCOUNT SET "
                    + "Username = ?, "
                    + "FullName = ?, "
                    + "Role = ?, "
                    + "Active = ?, "
                    + "PasswordHash = ? "
                    + "WHERE UserID = ?";
        } else {
            sql = "UPDATE USER_ACCOUNT SET "
                    + "Username = ?, "
                    + "FullName = ?, "
                    + "Role = ?, "
                    + "Active = ? "
                    + "WHERE UserID = ?";
        }

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, fullName);
            statement.setString(3, role);

            statement.setBoolean(
                    4,
                    chkUserActive.isSelected()
            );

            if (changePassword) {

                statement.setString(
                        5,
                        PasswordUtil.hash(password)
                );

                statement.setInt(
                        6,
                        selectedUserId
                );

            } else {
                statement.setInt(
                        5,
                        selectedUserId
                );
            }

            int affectedRows
                    = statement.executeUpdate();

            if (affectedRows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "User account updated successfully."
                );

                loadUsers();
                clearUserFields();
            }

        } catch (SQLException exception) {
            showUserDatabaseError(
                    "Could not update the user account.",
                    exception
            );
        }
    }

    private void deactivateUser() {

        if (selectedUserId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a user from the table first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmation
                = JOptionPane.showConfirmDialog(
                        this,
                        "Deactivate this user account?",
                        "Confirm Deactivation",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String sql
                = "UPDATE USER_ACCOUNT "
                + "SET Active = 0 "
                + "WHERE UserID = ?";

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    selectedUserId
            );

            int affectedRows
                    = statement.executeUpdate();

            if (affectedRows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "User account deactivated successfully."
                );

                loadUsers();
                clearUserFields();
            }

        } catch (SQLException exception) {
            showUserDatabaseError(
                    "Could not deactivate the user account.",
                    exception
            );
        }
    }

    private void clearUserFields() {

        selectedUserId = null;

        tblUsers.clearSelection();

        txtUsername.setText("");
        txtFullName.setText("");
        txtUserPassword.setText("");

        if (cmbUserRole.getItemCount() > 0) {
            cmbUserRole.setSelectedIndex(-1);
        }

        chkUserActive.setSelected(true);

        txtUsername.requestFocus();
    }

    private void showUserDatabaseError(
            String message,
            SQLException exception) {

        JOptionPane.showMessageDialog(
                this,
                message + "\n\n"
                + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );

        exception.printStackTrace();
    }

    private void configureSalesReportTable() {

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Sale ID",
                    "Date & Time",
                    "Sales Assistant",
                    "Items",
                    "Total Amount"
                }
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tblSalesReport.setModel(model);
        tblSalesReport.setRowHeight(25);

        tblSalesReport.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void loadReportTypes() {

        cmbReportType.removeAllItems();

        cmbReportType.addItem("DAILY");
        cmbReportType.addItem("MONTHLY");

        cmbReportType.setSelectedItem("DAILY");
    }

    private void generateSalesReport() {

        String dateText
                = txtReportDate.getText().trim();

        if (dateText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a report date.",
                    "Date Required",
                    JOptionPane.WARNING_MESSAGE
            );

            txtReportDate.requestFocus();
            return;
        }

        try {
            LocalDate selectedDate
                    = LocalDate.parse(dateText);

            String reportType
                    = cmbReportType
                            .getSelectedItem()
                            .toString();

            LocalDate startDate;
            LocalDate endDate;

            if ("MONTHLY".equals(reportType)) {

                startDate = selectedDate.withDayOfMonth(1);
                endDate = startDate.plusMonths(1);

            } else {

                startDate = selectedDate;
                endDate = selectedDate.plusDays(1);
            }

            loadSalesReport(startDate, endDate);

        } catch (DateTimeParseException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter the date using YYYY-MM-DD.\n"
                    + "Example: 2026-09-09",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            txtReportDate.requestFocus();
        }
    }

    private void loadSalesReport(
            LocalDate startDate,
            LocalDate endDate) {

        String sql
                = "SELECT s.SaleID, "
                + "s.SaleDateTime, "
                + "u.FullName, "
                + "COALESCE(SUM(si.Quantity), 0) "
                + "AS ItemsSold, "
                + "s.TotalAmount "
                + "FROM SALE s "
                + "INNER JOIN USER_ACCOUNT u "
                + "ON s.UserID = u.UserID "
                + "LEFT JOIN SALE_ITEM si "
                + "ON s.SaleID = si.SaleID "
                + "WHERE s.SaleDateTime >= ? "
                + "AND s.SaleDateTime < ? "
                + "GROUP BY s.SaleID, "
                + "s.SaleDateTime, "
                + "u.FullName, "
                + "s.TotalAmount "
                + "ORDER BY s.SaleDateTime DESC";

        DefaultTableModel model
                = (DefaultTableModel) tblSalesReport.getModel();

        model.setRowCount(0);

        int totalSales = 0;
        int totalItems = 0;

        BigDecimal totalRevenue
                = BigDecimal.ZERO;

        try (Connection connection
                = DBConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setTimestamp(
                    1,
                    Timestamp.valueOf(
                            startDate.atStartOfDay()
                    )
            );

            statement.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            endDate.atStartOfDay()
                    )
            );

            try (ResultSet result
                    = statement.executeQuery()) {

                while (result.next()) {

                    int itemsSold
                            = result.getInt("ItemsSold");

                    BigDecimal saleTotal
                            = result.getBigDecimal(
                                    "TotalAmount"
                            );

                    if (saleTotal == null) {
                        saleTotal = BigDecimal.ZERO;
                    }

                    Object[] row = {
                        result.getInt("SaleID"),
                        result.getTimestamp(
                        "SaleDateTime"
                        ),
                        result.getString("FullName"),
                        itemsSold,
                        String.format(
                        "Rs. %,.2f",
                        saleTotal
                        )
                    };

                    model.addRow(row);

                    totalSales++;
                    totalItems += itemsSold;

                    totalRevenue
                            = totalRevenue.add(saleTotal);
                }
            }

            displayReportTotals(
                    totalSales,
                    totalItems,
                    totalRevenue
            );

            if (totalSales == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No sales were found for "
                        + "the selected period.",
                        "No Sales",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException exception) {

            showReportDatabaseError(
                    "Could not load the sales report.",
                    exception
            );
        }
    }

    private void displayReportTotals(
            int totalSales,
            int totalItems,
            BigDecimal totalRevenue) {

        String revenueText
                = String.format(
                        "Rs. %,.2f",
                        totalRevenue
                );

        lblTotalSalesValue.setText(
                String.valueOf(totalSales)
        );

        lblItemsSoldValue.setText(
                String.valueOf(totalItems)
        );

        lblTotalRevenueValue.setText(
                revenueText
        );

        lblGrandTotalValue.setText(revenueText);
    }

    private void viewTodaySalesReport() {

        LocalDate today
                = LocalDate.now();

        cmbReportType.setSelectedItem("DAILY");

        txtReportDate.setText(
                today.toString()
        );

        loadSalesReport(
                today,
                today.plusDays(1)
        );
    }

    private void refreshSalesReport() {

        generateSalesReport();
    }

    private void showReportDatabaseError(
            String message,
            SQLException exception) {

        JOptionPane.showMessageDialog(
                this,
                message + "\n\n"
                + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );

        exception.printStackTrace();
    }

    public void applyUserRole(String role) {

        boolean isStoreManager
                = "STORE_MANAGER".equalsIgnoreCase(role);

        if (isStoreManager) {

            lblUserRole.setText("Store Manager");

            btnPromotions.setVisible(true);
            btnUsers.setVisible(true);
            btnReports.setVisible(true);
            btnUpdateMusicItem.setVisible(true);

        } else {

            lblUserRole.setText("Sales Assistant");

            btnPromotions.setVisible(false);
            btnUsers.setVisible(false);
            btnReports.setVisible(false);

            // Updating prices is a Store Manager function.
            btnUpdateMusicItem.setVisible(false);

            removeDashboardTab(tabPromotions);
            removeDashboardTab(tabUsers);
            removeDashboardTab(tabReports);

            dashboardTabs.setSelectedComponent(tabHome);
        }

        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    private void removeDashboardTab(java.awt.Component tab) {

        int tabIndex = dashboardTabs.indexOfComponent(tab);

        if (tabIndex >= 0) {
            dashboardTabs.removeTabAt(tabIndex);
        }
    }

}
