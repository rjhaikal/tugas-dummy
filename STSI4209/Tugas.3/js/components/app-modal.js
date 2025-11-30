Vue.component('app-modal', {
    template: '#tpl-modal',
    data() {
        return {
            isVisible: false,
            title: '',
            message: '',
            onConfirm: null
        };
    },
    methods: {
        show(title, message, onConfirm) {
            this.title = title;
            this.message = message;
            this.onConfirm = onConfirm;
            this.isVisible = true;
        },
        hide() {
            this.isVisible = false;
            this.onConfirm = null;
        },
        confirm() {
            if (this.onConfirm) {
                this.onConfirm();
            }
            this.hide();
        }
    }
});