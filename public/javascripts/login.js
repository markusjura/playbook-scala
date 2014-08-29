/** @jsx React.DOM */

(function () {

  var Input = React.createClass({
    getInitialState: function() { return {}; },

    getValue: function() {
      return this.refs.input.getDOMNode().value;
    },

    renderInput: function(){
      var className = "form-control";
      return <input name={this.props.name} type={this.props.type} className={className}
        placeholder={this.props.placeholder} value={this.props.value} ref="input" />;
    },

    renderLabel: function(){
      return <label>{this.props.label}</label> ? this.props.label : undefined;
    },

    renderError: function(error) {
      return <p className="help-block">{error}</p>
    },

    validate: function() {
      var value = this.getValue();
      var error;
      if (this.props.required && !value)
        error = 'required';
      else if (this.props.minLength && value.length < this.props.minLength)
        error = 'minLength';
      this.setState({error: error});

      return error;
    },

    onBlur: function(e){
      var error = this.validate();
      this.setState({showError: true});
      this.props.onUserInput(e.target, error);
    },

    onChange: function(e) {
      var error = this.validate();
      this.props.onUserInput(e.target, error);
    },

    render: function() {
      var className = "form-group";
      var errorMsg;
      if (this.state.error && this.state.showError) {
        className += ' has-error';
        errorMsg = this.props.error;
      }

      return (
        <div className={className} onBlur={this.onBlur} onChange={this.onChange}>
          {this.renderLabel()}
          {this.renderInput()}
          {this.renderError(errorMsg)}
        </div>
        );
    }
  });

  var Button = React.createClass({
    render: function() {
      return <button className="btn btn-primary btn-lg" type="submit"
        disabled={this.props.disabled}>{this.props.children}</button>
    }
  });

  var GlobalError = React.createClass({
    render: function() {
      return (
        <div className="has-error">
          <p className="help-block">{this.props.children}</p>
        </div>
      );
    }
  });

  var LoginForm = React.createClass({
    getInitialState: function() {
      var errors = {};
      if(!uid) errors = this.updateErrors(errors, 'required', 'uid');
      if(!password) errors = this.updateErrors(errors, 'required', 'password');

      return {
        uid: uid,
        password: password,
        errors: errors,
        buttonDisabled: this.updateButtonDisabled(errors)
      };
    },

    handleUserInput: function(inputField, error) {
      // Update input value
      if(inputField.name == "uid")
        this.setState({uid: inputField.value});
      else if(inputField.name == "password")
        this.setState({password: inputField.value});

      // Update errors
      var errors = this.updateErrors(this.state.errors, error, inputField.name);
      this.setState({errors: errors});

      // Update buttonDisabled
      this.setState({buttonDisabled: this.updateButtonDisabled(errors)});
    },

    updateErrors: function(errors, error, key) {
      (error) ? errors[key] = error : delete errors[key];
      return errors;
    },

    updateButtonDisabled: function(errors) {
      return ($.isEmptyObject(errors)) ? false : true;
    },

    render: function() {
      return (
        <form name="loginForm" action="/login" method="post">
          <GlobalError>{globalError}</GlobalError>
          <Input name="uid" type="text" placeholder="User id" onUserInput={this.handleUserInput} value={this.state.uid}
            required={true} minLength={6} error="User id has a minimum length of 6." />
          <Input name="password" type="password" placeholder="Password" onUserInput={this.handleUserInput} value={this.state.password}
            required={true} minLength={4} error="Password has a minimum length of 4."/>
          <Button disabled={this.state.buttonDisabled}>Log In</Button>
        </form>
      );
    }
  });

  React.renderComponent(
    <LoginForm />,
    document.getElementById('login-form')
  );
})();
